<script setup>
import {ref, reactive, onMounted<#if !readOnly>, computed</#if>} from 'vue'
import {<#if !readOnly>Plus, </#if>Refresh} from '@element-plus/icons-vue'
import {ElMessage<#if !readOnly>, ElMessageBox</#if>} from 'element-plus'
import {<#if !readOnly>save${Domain}, delete${Domain}, </#if>list${Domain}} from '@/api/${module}/${domain}.js'

// ${tableNameCn}列表数据
const ${domain}List = ref([])

<#if !readOnly>
// 控制新增对话框的显示
const dialogVisible = ref(false)

// 新增${tableNameCn}表单数据
const ${domain}Form = reactive({
<#list fieldList as field>
  <#if field.nameLowerCamelCase != 'userId' && field.nameLowerCamelCase != 'password' && field.nameLowerCamelCase != 'createdAt' && field.nameLowerCamelCase != 'updatedAt' && field.nameLowerCamelCase != 'deletedAt'>
    <#if field.type?contains('int')>
  ${field.nameLowerCamelCase}: <#if field.defaultValue?has_content>${field.defaultValue}<#else>0</#if>,
    <#else>
  ${field.nameLowerCamelCase}: <#if field.defaultValue?has_content>${field.defaultValue}<#else>null</#if>,
    </#if>
  </#if>
</#list>
})

// 表单规则
const rules = {
<#list fieldList as field>
  <#if field.nameLowerCamelCase != 'id' && field.nameLowerCamelCase != 'userId' && field.nameLowerCamelCase != 'password' && field.nameLowerCamelCase != 'createdAt' && field.nameLowerCamelCase != 'updatedAt' && field.nameLowerCamelCase != 'deletedAt'>
  ${field.nameLowerCamelCase}: [
    {required: <#if field.nullAble>false<#else>true</#if>, message: '请输入${field.nameCn}', trigger: 'blur'},
    <#if field.nameLowerCamelCase == "idCard">
    {pattern: /^\d{18}$/, message: '身份证号必须为18位数字', trigger: 'blur'}
    </#if>
    <#if field.nameLowerCamelCase == "mobile">
    {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur'}
    </#if>
    <#if field.nameLowerCamelCase == "email">
    {type: 'email', message: '请输入有效的邮箱', trigger: 'blur'}
    </#if>
  ],
  </#if>
</#list>
}

const formRef = ref(null)
const tableRef = ref(null)

const handleAdd = () => {
  dialogVisible.value = true
}

const resetForm = () => {
<#list fieldList as field>
  <#if field.nameLowerCamelCase != 'userId' && field.nameLowerCamelCase != 'password' && field.nameLowerCamelCase != 'createdAt' && field.nameLowerCamelCase != 'updatedAt' && field.nameLowerCamelCase != 'deletedAt'>
    <#if field.type?contains('int')>
  ${domain}Form.${field.nameLowerCamelCase} = <#if field.defaultValue?has_content>${field.defaultValue}<#else>0</#if>
    <#else>
  ${domain}Form.${field.nameLowerCamelCase} = <#if field.defaultValue?has_content>${field.defaultValue}<#else>null</#if>
    </#if>
  </#if>
</#list>
}

const submitForm = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    const res = await save${Domain}(${domain}Form)
    if (res.code === 200) {
      ElMessage.success(${domain}Form.id ? '编辑成功' : '添加成功')
      dialogVisible.value = false
      resetForm()
      // 刷新${tableNameCn}列表
      pagination.page = 1
      await get${Domain}List()
    } else {
      // 场景：http 返回头的状态码是 200，但是返回体中的 code 字段值不是 200
      ElMessage.error(res.msg || (${domain}Form.id ? '编辑${tableNameCn}失败' : '添加${tableNameCn}失败'))
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || (${domain}Form.id ? '编辑${tableNameCn}失败' : '添加${tableNameCn}失败'))
  }
}

const handleEdit = (row) => {
  Object.assign(${domain}Form, row)
  dialogVisible.value = true
}

const dialogTitle = computed(() => {
  return ${domain}Form.id ? '编辑${tableNameCn}' : '新增${tableNameCn}'
})

// 删除${tableNameCn}
const handleDelete = async (ids) => {
  try {
    const res = await delete${Domain}(ids)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      // 如果当前页只剩要被删除的条数且不是第一页，删除后自动跳转到上一页
      if (${domain}List.value.length === ids.length && pagination.page > 1) {
        pagination.page--
      }
      await get${Domain}List()
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
          '确定要删除${tableNameCn} ' + row.id + ' 吗？',
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
</#if>

const tableLoading = ref(false)

// 搜索表单
const searchForm = reactive({
  keyword: ''  // 搜索关键字
})

// 获取${tableNameCn}列表数据
const get${Domain}List = async () => {
  tableLoading.value = true
  try {
    const res = await list${Domain}({
      ...pagination,
      keyword: searchForm.keyword
    })
    if (res.code === 200) {
      ${domain}List.value = res.data.list
      pagination.total = res.data.total
    } else {
      ElMessage.error(res.msg || '获取${tableNameCn}列表失败')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '获取${tableNameCn}列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 在组件挂载时获取数据
onMounted(() => {
  get${Domain}List()
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
  await get${Domain}List()
}

const handleSizeChange = async (val) => {
  pagination.size = val
  pagination.page = 1
  await get${Domain}List()
}

<#list fieldList as field>
  <#if field.enums>
const ${field.enumsConst}_ARRAY = window.${field.enumsConst}_ARRAY
const get${field.enumsClass}EnumDesc = (type) => {
  return ${field.enumsConst}_ARRAY.find(option => option.code === type)?.desc || ''
}
  </#if>
</#list>

const handleRefresh = () => {
  get${Domain}List()
}

// 处理搜索
const handleSearch = () => {
  pagination.page = 1  // 重置到第一页
  get${Domain}List()
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  handleSearch()
}
</script>

<template>
  <div class="${domain}-container">
    <!-- 顶部按钮 -->
    <div class="top-tools">
      <div class="left">
        <#if !readOnly>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增${tableNameCn}</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="!hasSelectedRows">批量删除</el-button>
        </#if>
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

    <!-- ${tableNameCn}列表 -->
    <el-table
      <#if !readOnly>ref="tableRef"</#if>
      :data="${domain}List"
      v-loading="tableLoading"
      :table-layout="'auto'"
      style="width: 100%"
      border
    >
      <#if !readOnly><el-table-column type="selection"></el-table-column></#if>
      <#list fieldList as field>
        <#if field.nameLowerCamelCase != 'id' && field.nameLowerCamelCase != 'userId' && field.nameLowerCamelCase != 'password' && field.nameLowerCamelCase != 'createdAt' && field.nameLowerCamelCase != 'updatedAt' && field.nameLowerCamelCase != 'deletedAt'>
          <#if field.enums>
      <el-table-column prop="${field.nameLowerCamelCase}" label="${field.nameCn}">
        <template #default="{ row }">
          {{ get${field.enumsClass}EnumDesc(row.${field.nameLowerCamelCase}) }}
        </template>
      </el-table-column>
          <#else>
      <el-table-column prop="${field.nameLowerCamelCase}" label="${field.nameCn}"/>
          </#if>
        </#if>
      </#list>
      <#if !readOnly>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleSingleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      </#if>
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

    <#if !readOnly>
    <!-- 新增${tableNameCn}对话框 -->
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
        :model="${domain}Form"
        :rules="rules"
        label-width="100px"
        status-icon
      >
      <#list fieldList as field>
        <#if field.nameLowerCamelCase != 'id' && field.nameLowerCamelCase != 'userId' && field.nameLowerCamelCase != 'password' && field.nameLowerCamelCase != 'createdAt' && field.nameLowerCamelCase != 'updatedAt' && field.nameLowerCamelCase != 'deletedAt'>
        <el-form-item label="${field.nameCn}" prop="${field.nameLowerCamelCase}">
          <#if field.enums>
          <el-select
            v-model="${domain}Form.${field.nameLowerCamelCase}"
            style="width: 100%"
            placeholder="请选择${field.nameCn}"
          >
            <el-option
              v-for="option in ${field.enumsConst}_ARRAY"
              :key="option.code"
              :label="option.desc"
              :value="option.code"
            />
          </el-select>
          <#elseif field.javaType == 'Date'>
            <#if field.type == 'time'>
          <el-time-picker
            v-model="${domain}Form.${field.nameLowerCamelCase}"
            placeholder="请输入${field.nameCn}"
            value-format="HH:mm:ss"
          />
            <#elseif field.type == 'date'>
          <el-date-picker
            v-model="${domain}Form.${field.nameLowerCamelCase}"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请输入${field.nameCn}"
          />
            <#else >
          <el-date-picker
            v-model="${domain}Form.${field.nameLowerCamelCase}"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请输入${field.nameCn}"
          />
            </#if>
          <#elseif field.type == 'int' || field.type == 'int unsigned'>
          <el-input-number v-model="${domain}Form.${field.nameLowerCamelCase}" />
          <#elseif field.type == 'tinyint'>
          <el-switch v-model="${domain}Form.${field.nameLowerCamelCase}" :active-value="1" :inactive-value="0" />
          <#else>
          <el-input
            v-model="${domain}Form.${field.nameLowerCamelCase}"
            placeholder="请输入${field.nameCn}"
            clearable
          />
          </#if>
        </el-form-item>
        </#if>
      </#list>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    </#if>
  </div>
</template>

<style scoped>
.${domain}-container {
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
