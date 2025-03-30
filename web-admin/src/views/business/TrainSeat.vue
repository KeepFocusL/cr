<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import {Plus, Refresh} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {saveTrainSeat, deleteTrainSeat, listTrainSeat} from '@/api/business/trainSeat.js'

// 车厢座位列表数据
const trainSeatList = ref([])

// 控制新增对话框的显示
const dialogVisible = ref(false)

// 新增车厢座位表单数据
const trainSeatForm = reactive({
  id: 0,
  trainCode: null,
  carriageIndex: 0,
  row: null,
  col: null,
  seatType: null,
  carriageSeatIndex: 0,
})

// 表单规则
const rules = {
  trainCode: [
    {required: true, message: '请输入车次编号', trigger: 'blur'},
  ],
  carriageIndex: [
    {required: true, message: '请输入厢序', trigger: 'blur'},
  ],
  row: [
    {required: true, message: '请输入排号', trigger: 'blur'},
  ],
  col: [
    {required: true, message: '请输入列号', trigger: 'blur'},
  ],
  seatType: [
    {required: true, message: '请输入座位类型', trigger: 'blur'},
  ],
  carriageSeatIndex: [
    {required: true, message: '请输入同车厢座序', trigger: 'blur'},
  ],
}

const formRef = ref(null)
const tableRef = ref(null)

const handleAdd = () => {
  dialogVisible.value = true
}

const resetForm = () => {
  trainSeatForm.id = 0
  trainSeatForm.trainCode = null
  trainSeatForm.carriageIndex = 0
  trainSeatForm.row = null
  trainSeatForm.col = null
  trainSeatForm.seatType = null
  trainSeatForm.carriageSeatIndex = 0
}

const submitForm = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    const res = await saveTrainSeat(trainSeatForm)
    if (res.code === 200) {
      ElMessage.success(trainSeatForm.id ? '编辑成功' : '添加成功')
      dialogVisible.value = false
      resetForm()
      // 刷新车厢座位列表
      pagination.page = 1
      await getTrainSeatList()
    } else {
      // 场景：http 返回头的状态码是 200，但是返回体中的 code 字段值不是 200
      ElMessage.error(res.msg || (trainSeatForm.id ? '编辑车厢座位失败' : '添加车厢座位失败'))
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || (trainSeatForm.id ? '编辑车厢座位失败' : '添加车厢座位失败'))
  }
}

const handleEdit = (row) => {
  Object.assign(trainSeatForm, row)
  dialogVisible.value = true
}

const dialogTitle = computed(() => {
  return trainSeatForm.id ? '编辑车厢座位' : '新增车厢座位'
})

// 删除车厢座位
const handleDelete = async (ids) => {
  try {
    const res = await deleteTrainSeat(ids)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      // 如果当前页只剩要被删除的条数且不是第一页，删除后自动跳转到上一页
      if (trainSeatList.value.length === ids.length && pagination.page > 1) {
        pagination.page--
      }
      await getTrainSeatList()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '删除失败')
  }
}

// 处理单个删除 - 二次确认
const handleSingleDelete = (row) => {
  ElMessageBox.confirm(
          '确定要删除车厢座位 ' + row.id + ' 吗？',
          '删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
  )
          .then(() => handleDelete([row.id]))
          .catch(() => {
            console.log('取消删除，不做任何操作')
          })
}

// 计算属性：检查是否有选中的行
const hasSelectedRows = computed(() => {
  return tableRef.value ? tableRef.value.getSelectionRows().length > 0 : false
})

// 处理批量删除 - 二次确认
const handleBatchDelete = async () => {
  const selectedRows = tableRef.value.getSelectionRows()
  if (!selectedRows || selectedRows.length === 0) {
    ElMessage.warning('请先选择要删除的记录')
    return
  }
  // 弹出确认对话框
  ElMessageBox.confirm(
          '确定要删除选中的 ' + selectedRows.length + ' 记录吗？',
          '删除确认',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
  )
          .then(() => handleDelete(selectedRows.map(item => item.id)))
          .catch(() => {
            console.log('取消删除，不做任何操作')
          })
}

const tableLoading = ref(false)

// 搜索表单
const searchForm = reactive({
  keyword: ''  // 搜索关键字
})

// 获取车厢座位列表数据
const getTrainSeatList = async () => {
  tableLoading.value = true
  try {
    const res = await listTrainSeat({
      ...pagination,
      keyword: searchForm.keyword
    })
    if (res.code === 200) {
      trainSeatList.value = res.data.list
      pagination.total = res.data.total
    } else {
      ElMessage.error(res.msg || '获取车厢座位列表失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '获取车厢座位列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 在组件挂载时获取数据
onMounted(() => {
  getTrainSeatList()
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
  await getTrainSeatList()
}

const handleSizeChange = async (val) => {
  pagination.size = val
  pagination.page = 1
  await getTrainSeatList()
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
  getTrainSeatList()
}

// 处理搜索
const handleSearch = () => {
  pagination.page = 1  // 重置到第一页
  getTrainSeatList()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  handleSearch()
}
</script>

<template>
  <div class="trainSeat-container">
    <!-- 顶部按钮 -->
    <div class="top-tools">
      <div class="left">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增车厢座位</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="!hasSelectedRows">批量删除</el-button>
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

    <!-- 车厢座位列表 -->
    <el-table
      ref="tableRef"
      :data="trainSeatList"
      v-loading="tableLoading"
      :table-layout="'auto'"
      style="width: 100%"
      border
    >
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="trainCode" label="车次编号"/>
      <el-table-column prop="carriageIndex" label="厢序"/>
      <el-table-column prop="row" label="排号"/>
      <el-table-column prop="col" label="列号">
        <template #default="{ row }">
          {{ getSeatColEnumDesc(row.col) }}
        </template>
      </el-table-column>
      <el-table-column prop="seatType" label="座位类型">
        <template #default="{ row }">
          {{ getSeatTypeEnumDesc(row.seatType) }}
        </template>
      </el-table-column>
      <el-table-column prop="carriageSeatIndex" label="同车厢座序"/>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleSingleDelete(row)">删除</el-button>
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

    <!-- 新增车厢座位对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      destroy-on-close
      @closed="resetForm"
    >
      <el-form
        ref="formRef"
        :model="trainSeatForm"
        :rules="rules"
        label-width="100px"
        status-icon
      >
        <el-form-item label="车次编号" prop="trainCode">
          <el-input
            v-model="trainSeatForm.trainCode"
            placeholder="请输入车次编号"
            clearable
          />
        </el-form-item>
        <el-form-item label="厢序" prop="carriageIndex">
          <el-input-number v-model="trainSeatForm.carriageIndex" />
        </el-form-item>
        <el-form-item label="排号" prop="row">
          <el-input
            v-model="trainSeatForm.row"
            placeholder="请输入排号"
            clearable
          />
        </el-form-item>
        <el-form-item label="列号" prop="col">
          <el-select
            v-model="trainSeatForm.col"
            style="width: 100%"
            placeholder="请选择列号"
          >
            <el-option
              v-for="option in SEAT_COL_ARRAY"
              :key="option.code"
              :label="option.desc"
              :value="option.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="座位类型" prop="seatType">
          <el-select
            v-model="trainSeatForm.seatType"
            style="width: 100%"
            placeholder="请选择座位类型"
          >
            <el-option
              v-for="option in SEAT_TYPE_ARRAY"
              :key="option.code"
              :label="option.desc"
              :value="option.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="同车厢座序" prop="carriageSeatIndex">
          <el-input-number v-model="trainSeatForm.carriageSeatIndex" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.trainSeat-container {
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
