<script setup>
import {ref, reactive, onMounted, computed} from 'vue'
import {Plus, Refresh} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {saveTrain, deleteTrain, listTrain, genSeat} from '@/api/business/train.js'
import {genDailyData} from '@/api/business/dailyTrain.js'
import StationSelect from "@/components/StationSelect.vue";
import {pinyin} from "pinyin-pro";
import dayjs from "dayjs";

// 车次列表数据
const trainList = ref([])

// 控制新增对话框的显示
const dialogVisible = ref(false)

// 新增车次表单数据
const trainForm = reactive({
  id: 0,
  code: null,
  type: null,
  start: null,
  startPinyin: null,
  startTime: null,
  end: null,
  endPinyin: null,
  endTime: null,
})

// 表单规则
const rules = {
  code: [
    {required: true, message: '请输入车次编号', trigger: 'blur'},
  ],
  type: [
    {required: true, message: '请输入车次类型', trigger: 'blur'},
  ],
  start: [
    {required: true, message: '请输入始发站', trigger: 'blur'},
  ],
  startPinyin: [
    {required: true, message: '请输入始发站拼音', trigger: 'blur'},
  ],
  startTime: [
    {required: true, message: '请输入出发时间', trigger: 'blur'},
  ],
  end: [
    {required: true, message: '请输入终点站', trigger: 'blur'},
  ],
  endPinyin: [
    {required: true, message: '请输入终点站拼音', trigger: 'blur'},
  ],
  endTime: [
    {required: true, message: '请输入到站时间', trigger: 'blur'},
  ],
}

const formRef = ref(null)
const tableRef = ref(null)

const handleAdd = () => {
  dialogVisible.value = true
}

const resetForm = () => {
  trainForm.id = 0
  trainForm.code = null
  trainForm.type = null
  trainForm.start = null
  trainForm.startPinyin = null
  trainForm.startTime = null
  trainForm.end = null
  trainForm.endPinyin = null
  trainForm.endTime = null
}

const submitForm = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    const res = await saveTrain(trainForm)
    if (res.code === 200) {
      ElMessage.success(trainForm.id ? '编辑成功' : '添加成功')
      dialogVisible.value = false
      resetForm()
      // 刷新车次列表
      pagination.page = 1
      await getTrainList()
    } else {
      // 场景：http 返回头的状态码是 200，但是返回体中的 code 字段值不是 200
      ElMessage.error(res.msg || (trainForm.id ? '编辑车次失败' : '添加车次失败'))
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || (trainForm.id ? '编辑车次失败' : '添加车次失败'))
  }
}

const handleEdit = (row) => {
  Object.assign(trainForm, row)
  dialogVisible.value = true
}

const dialogTitle = computed(() => {
  return trainForm.id ? '编辑车次' : '新增车次'
})

// 删除车次
const handleDelete = async (ids) => {
  try {
    const res = await deleteTrain(ids)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      // 如果当前页只剩要被删除的条数且不是第一页，删除后自动跳转到上一页
      if (trainList.value.length === ids.length && pagination.page > 1) {
        pagination.page--
      }
      await getTrainList()
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
    '确定要删除车次 ' + row.id + ' 吗？',
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

// 获取车次列表数据
const getTrainList = async () => {
  tableLoading.value = true
  try {
    const res = await listTrain({
      ...pagination,
      keyword: searchForm.keyword
    })
    if (res.code === 200) {
      trainList.value = res.data.list
      pagination.total = res.data.total
    } else {
      ElMessage.error(res.msg || '获取车次列表失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '获取车次列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 在组件挂载时获取数据
onMounted(() => {
  getTrainList()
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
  await getTrainList()
}

const handleSizeChange = async (val) => {
  pagination.size = val
  pagination.page = 1
  await getTrainList()
}

const TRAIN_TYPE_ARRAY = window.TRAIN_TYPE_ARRAY
const getTrainTypeEnumDesc = (type) => {
  return TRAIN_TYPE_ARRAY.find(option => option.code === type)?.desc || ''
}

const handleRefresh = () => {
  getTrainList()
}

// 处理搜索
const handleSearch = () => {
  pagination.page = 1  // 重置到第一页
  getTrainList()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  handleSearch()
}

const handleDailyData = () => {
  const date = selectedDate.value;

  ElMessageBox.confirm(
    `确定要生成【${selectedDate.value}】的每日数据吗？`,
    '生成确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(() => {
      genDailyData(date)
        .then((res) => {
          ElMessage.success('生成每日数据成功')
        })
        .catch((error) => {
          ElMessage.error(error.response?.data?.msg || '生成每日数据失败')
        })
    })
    .catch(() => {
      console.log('取消生成，不做任何操作')
    })
}

// 生成座位
const handleGenSeat = (row) => {
  ElMessageBox.confirm(
    `确定要为车次 ${row.code} 生成座位吗？`,
    '操作确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await genSeat(row.code)
      if (res.code === 200) {
        ElMessage.success('座位生成成功')
      } else {
        ElMessage.error(res.msg || '座位生成失败')
      }
    } catch (error) {
      console.error('座位生成失败:', error)
      ElMessage.error('座位生成失败')
    }
  }).catch(() => {
    //console.log('取消操作，不做任何处理');
  })
}

// 监听始发站变化，自动填充拼音
const handleStartChange = (value) => {
  if (!value) {
    trainForm.startPinyin = ''
    return
  }
  trainForm.startPinyin = pinyin(value, { toneType: 'none', type: 'string' }).replace(/\s/g, '')
}

// 监听始发站变化，自动填充拼音
const handleEndChange = (value) => {
  if (!value) {
    trainForm.endPinyin = ''
    return
  }
  trainForm.endPinyin = pinyin(value, { toneType: 'none', type: 'string' }).replace(/\s/g, '')
}

// 日期选择
const selectedDate = ref(dayjs().add(15, 'day').format('YYYY-MM-DD'))
</script>

<template>
  <div class="train-container">
    <!-- 顶部按钮 -->
    <div class="top-tools">
      <div class="left">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增车次</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="!hasSelectedRows">批量删除</el-button>
        <el-button :icon="Refresh" @click="handleRefresh">刷新</el-button>
      </div>
      <div class="right">
        <el-form :inline="true">
          <el-form-item class="mb0">
            <el-date-picker
              v-model="selectedDate"
              type="date"
              value-format="YYYY-MM-DD"
              placeholder="选择日期"
            />
          </el-form-item>
          <el-form-item class="mb0">
            <el-button
              type="primary"
              :icon="Calendar"
              @click="handleDailyData"
            >
              生成每日数据
            </el-button>
          </el-form-item>
        </el-form>
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

    <!-- 车次列表 -->
    <el-table
      ref="tableRef"
      :data="trainList"
      v-loading="tableLoading"
      :table-layout="'auto'"
      style="width: 100%"
      border
    >
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="code" label="车次编号"/>
      <el-table-column prop="type" label="车次类型">
        <template #default="{ row }">
          {{ getTrainTypeEnumDesc(row.type) }}
        </template>
      </el-table-column>
      <el-table-column prop="start" label="始发站"/>
      <el-table-column prop="startPinyin" label="始发站拼音"/>
      <el-table-column prop="startTime" label="出发时间"/>
      <el-table-column prop="end" label="终点站"/>
      <el-table-column prop="endPinyin" label="终点站拼音"/>
      <el-table-column prop="endTime" label="到站时间"/>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleSingleDelete(row)">删除</el-button>
          <el-button type="primary" link @click="handleGenSeat(row)">自动生成座位</el-button>
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

    <!-- 新增车次对话框 -->
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
        :model="trainForm"
        :rules="rules"
        label-width="100px"
        status-icon
      >
        <el-form-item label="车次编号" prop="code">
          <el-input
            v-model="trainForm.code"
            placeholder="请输入车次编号"
            clearable
          />
        </el-form-item>
        <el-form-item label="车次类型" prop="type">
          <el-select
            v-model="trainForm.type"
            style="width: 100%"
            placeholder="请选择车次类型"
          >
            <el-option
              v-for="option in TRAIN_TYPE_ARRAY"
              :key="option.code"
              :label="option.desc"
              :value="option.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="始发站" prop="start">
          <StationSelect
            v-model="trainForm.start"
            @update:modelValue="handleStartChange"
          />
        </el-form-item>
        <el-form-item label="始发站拼音" prop="startPinyin">
          <el-input
            v-model="trainForm.startPinyin"
            placeholder="请输入始发站拼音"
            clearable
          />
        </el-form-item>
        <el-form-item label="出发时间" prop="startTime">
          <el-time-picker
            v-model="trainForm.startTime"
            placeholder="请输入出发时间"
            value-format="HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="终点站" prop="end">
          <StationSelect
            v-model="trainForm.end"
            @update:modelValue="handleEndChange"
          />
        </el-form-item>
        <el-form-item label="终点站拼音" prop="endPinyin">
          <el-input
            v-model="trainForm.endPinyin"
            placeholder="请输入终点站拼音"
            clearable
          />
        </el-form-item>
        <el-form-item label="到站时间" prop="endTime">
          <el-time-picker
            v-model="trainForm.endTime"
            placeholder="请输入到站时间"
            value-format="HH:mm:ss"
          />
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
.train-container {
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
