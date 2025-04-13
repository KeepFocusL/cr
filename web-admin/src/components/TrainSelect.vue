<script setup>
import { ref } from 'vue'
import { listTrain } from '@/api/business/train'

const props = defineProps({
  modelValue: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    default: '不知道车次？可以输入起始站或终点站搜索车次'
  }
})

const emit = defineEmits(['update:modelValue'])

// 车次选项数据
const trainOptions = ref([])
const loading = ref(false)

// 远程搜索车次的方法
const handleSearch = async (query) => {
  if (!query) {
    trainOptions.value = []
    return
  }

  loading.value = true
  try {
    const res = await listTrain({
      page: 1,
      size: 20,
      keyword: query
    })
    if (res.code === 200) {
      trainOptions.value = res.data.list.map(item => ({
        value: item.code,
        label: `${item.code} (${item.start} -> ${item.end})`,
        start: item.start,
        end: item.end
      }))
    }
  } catch (error) {
    console.error('获取车次列表失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <el-select
    :model-value="modelValue"
    filterable
    remote
    reserve-keyword
    :placeholder="placeholder"
    :remote-method="handleSearch"
    :loading="loading"
    @update:modelValue="newValue => emit('update:modelValue', newValue)"
    style="width: 100%"
  >
    <el-option
      v-for="item in trainOptions"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    >
      <span style="float: left">{{ item.value }}</span>
      <span style="float: right; color: #8492a6; font-size: 13px">
        {{ item.start }} -> {{ item.end }}
      </span>
    </el-option>
  </el-select>
</template>
