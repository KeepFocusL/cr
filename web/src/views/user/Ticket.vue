<script setup>
import {ref, reactive, onMounted} from 'vue'
import {Refresh} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {listTicket} from '@/api/user/ticket.js'

// 会员车票列表数据
const ticketList = ref([])


const tableLoading = ref(false)

// 搜索表单
const searchForm = reactive({
  keyword: ''  // 搜索关键字
})

// 获取会员车票列表数据
const getTicketList = async () => {
  tableLoading.value = true
  try {
    const res = await listTicket({
      ...pagination,
      keyword: searchForm.keyword
    })
    if (res.code === 200) {
      ticketList.value = res.data.list
      pagination.total = res.data.total
    } else {
      ElMessage.error(res.msg || '获取会员车票列表失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '获取会员车票列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 在组件挂载时获取数据
onMounted(() => {
  getTicketList()
})

// 分页相关数据
const pagination = reactive({
  // 每页显示的数据条数
  size: 20,
  // 当前页码，从1开始
  page: 1,
  // 总数据条数，会在获取数据时更新
  total: 0
})

// 分页变化处理函数
const handleCurrentChange = async (val) => {
  pagination.page = val
  await getTicketList()
}

const handleSizeChange = async (val) => {
  pagination.size = val
  pagination.page = 1
  await getTicketList()
}

const SEAT_COL_ARRAY = window.SEAT_COL_ARRAY
const getSeatColEnumDesc = (type) => {
  return SEAT_COL_ARRAY.find(option => option.code === type)?.desc || ''
}
const SEAT_TYPE_ARRAY = window.SEAT_TYPE_ARRAY
const getSeatTypeEnumDesc = (type) => {
  return SEAT_TYPE_ARRAY.find(option => option.code === type)?.desc || ''
}

const handleRefresh = () => {
  getTicketList()
}

// 处理搜索
const handleSearch = () => {
  pagination.page = 1  // 重置到第一页
  getTicketList()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  handleSearch()
}
</script>

<template>
  <div class="ticket-container">
    <!-- 顶部按钮 -->
    <div class="top-tools">
      <div class="left">
        <el-button :icon="Refresh" @click="handleRefresh">刷新</el-button>
      </div>
      <div class="right">
        <el-form :inline="true" :model="searchForm" @submit.prevent>
          <el-form-item class="mb0">
            <el-input
                    v-model="searchForm.keyword"
                    placeholder="请输入关键词"
                    clearable
            />
          </el-form-item>
          <el-form-item class="mb0">
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 会员车票列表 -->
    <el-table
      
      :data="ticketList"
      v-loading="tableLoading"
      :table-layout="'auto'"
      style="width: 100%"
      border
    >
      
      <el-table-column prop="passengerId" label="乘客id"/>
      <el-table-column prop="passengerName" label="乘客姓名"/>
      <el-table-column prop="trainDate" label="日期"/>
      <el-table-column prop="trainCode" label="车次编号"/>
      <el-table-column prop="carriageIndex" label="箱序"/>
      <el-table-column prop="seatRow" label="排号"/>
      <el-table-column prop="seatCol" label="列号">
        <template #default="{ row }">
          {{ getSeatColEnumDesc(row.seatCol) }}
        </template>
      </el-table-column>
      <el-table-column prop="startStation" label="出发站"/>
      <el-table-column prop="startTime" label="出发时间"/>
      <el-table-column prop="endStation" label="到达站"/>
      <el-table-column prop="endTime" label="到站时间"/>
      <el-table-column prop="seatType" label="座位类型">
        <template #default="{ row }">
          {{ getSeatTypeEnumDesc(row.seatType) }}
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :page-sizes="[2, 5, 10, 20]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

  </div>
</template>

<style scoped>
.ticket-container {
  padding: 20px;
}

.top-tools {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}

.top-tools .left {
  display: flex;
  gap: 10px;
}

.top-tools .right {
  display: flex;
  align-items: center;
  margin-left: auto;

  .el-form .el-form-item:last-child {
    margin-right: 0;
  }
}

@media screen and (max-width: 768px) {
  .top-tools {
    flex-direction: column;
    align-items: flex-start;
  }

  .top-tools .right {
    margin-left: 0;
    width: 100%;
  }
}

.mb0 {
  margin-bottom: 0;
}

.el-form--inline .el-form-item {
  margin-right: 16px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
