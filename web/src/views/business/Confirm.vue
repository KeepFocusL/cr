<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowRight } from '@element-plus/icons-vue'
import { useTicketStore } from '@/stores/ticket'
import router from "@/router/index.js";

const ticketStore = useTicketStore()

const route = useRoute()
const ticketInfo = ref({})
const selectedSeat = ref('')
const loading = ref(false)

// 座位类型选项
const seatOptions = [
  { label: '一等座', value: 'ydz', price: 0, count: 0 },
  { label: '二等座', value: 'edz', price: 0, count: 0 },
  { label: '软卧', value: 'rw', price: 0, count: 0 },
  { label: '硬卧', value: 'yw', price: 0, count: 0 }
]

onMounted(() => {
  // 从 store 获取车票信息
  if (!ticketStore.ticketInfo) {
    ElMessage.error('车票信息不存在')
    router.push('/ticket')
    return
  }

  ticketInfo.value = ticketStore.ticketInfo

  // 更新座位选项的价格和余票，并过滤掉余票为-1的选项
  seatOptions[0].price = ticketInfo.value.ydzPrice
  seatOptions[0].count = ticketInfo.value.ydz
  seatOptions[1].price = ticketInfo.value.edzPrice
  seatOptions[1].count = ticketInfo.value.edz
  seatOptions[2].price = ticketInfo.value.rwPrice
  seatOptions[2].count = ticketInfo.value.rw
  seatOptions[3].price = ticketInfo.value.ywPrice
  seatOptions[3].count = ticketInfo.value.yw
})

const handleSubmit = () => {
  if (!selectedSeat.value) {
    ElMessage.warning('请选择座位类型')
    return
  }
  loading.value = true
  // TODO: 处理提交订单逻辑
  setTimeout(() => {
    loading.value = false
    ElMessage.success('订单提交成功！')
    // 清除store中的数据
    ticketStore.clearTicketInfo()
  }, 1000)
}

// 计算行程时间
const calculateDuration = (startTime, endTime) => {
  if (!startTime || !endTime) return ''

  const getMinutes = (time) => {
    const [hours, minutes] = time.split(':').map(Number)
    return hours * 60 + minutes
  }

  let startMinutes = getMinutes(startTime)
  let endMinutes = getMinutes(endTime)

  // 如果结束时间小于开始时间，说明是跨天，需要加24小时
  if (endMinutes < startMinutes) {
    endMinutes += 24 * 60
  }

  const diffMinutes = endMinutes - startMinutes
  const hours = Math.floor(diffMinutes / 60)
  const minutes = diffMinutes % 60

  return `${hours}小时${minutes}分钟`
}

// 判断是否跨天
const isNextDay = (startTime, endTime) => {
  if (!startTime || !endTime) return false

  const getMinutes = (time) => {
    const [hours, minutes] = time.split(':').map(Number)
    return hours * 60 + minutes
  }
  return getMinutes(endTime) < getMinutes(startTime)
}
</script>

<template>
  <div class="confirm-container">
    <el-card class="ticket-card">
      <!-- 车次信息 -->
      <template #header>
        <div class="card-header">
          <el-tag size="large" type="primary">{{ ticketInfo.trainCode }}</el-tag>
          <span class="date">{{ ticketInfo.date }}</span>
        </div>
      </template>

      <!-- 行程信息 -->
      <div class="journey-info">
        <div class="station-info start">
          <div class="time">{{ ticketInfo.startTime }}</div>
          <div class="station">{{ ticketInfo.start }}</div>
          <div class="date">{{ ticketInfo.date }}</div>
        </div>
        <div class="journey-duration">
          <el-icon class="arrow-icon"><ArrowRight /></el-icon>
          <div class="duration">
            {{ calculateDuration(ticketInfo.startTime, ticketInfo.endTime) }}
          </div>
        </div>
        <div class="station-info end">
          <div class="time">{{ ticketInfo.endTime }}</div>
          <div class="station">{{ ticketInfo.end }}</div>
          <div class="date">{{ isNextDay(ticketInfo.startTime, ticketInfo.endTime) ? '次日到达' : '当日到达' }}</div>
        </div>
      </div>

      <!-- 座位选择 -->
      <div class="seat-selection">
        <h3>选择座位类型</h3>
        <div class="seat-options">
          <div
            v-for="option in seatOptions"
            :key="option.value"
            class="seat-option-card"
            v-show="option.count !== -1"
            :class="{ 'selected': selectedSeat === option.value }"
            @click="option.count > 0 && (selectedSeat = option.value)"
          >
            <div class="seat-type">{{ option.label }}</div>
            <div class="seat-price">￥{{ option.price }}</div>
            <el-tag
              :type="option.count > 0 ? 'success' : 'info'"
              size="small"
            >
              余票: {{ option.count }}
            </el-tag>
          </div>
        </div>
      </div>

      <!-- 乘客信息 -->
      <div class="passenger-info">
        <h3>乘客信息</h3>
        <el-alert
          title="请先选择乘车人"
          type="info"
          :closable="false"
          show-icon
        />
        <!-- TODO: 添加乘客选择组件 -->
      </div>

      <!-- 提交按钮 -->
      <div class="submit-section">
        <div class="price-summary" v-if="selectedSeat">
          <span class="label">总金额：</span>
          <span class="total-price">￥{{ seatOptions.find(o => o.value === selectedSeat)?.price }}</span>
        </div>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleSubmit"
          size="large"
        >
          提交订单
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.confirm-container {
  padding: 12px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.ticket-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 16px;
}

.date {
  color: #909399;
  font-size: 14px;
}

.seat-selection {
  margin-top: 20px;
}

.seat-selection h3 {
  margin-bottom: 15px;
  color: #303133;
  font-size: 16px;
}

.seat-options {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 12px;
}

.seat-option-card {
  padding: 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  gap: 8px;
  align-items: center;
  background-color: #fff;
}

.seat-option-card:hover {
  border-color: #409EFF;
}

.seat-option-card.selected {
  border-color: #409EFF;
  background-color: #ecf5ff;
}

.seat-option-card[disabled] {
  cursor: not-allowed;
  opacity: 0.6;
}

.seat-type {
  font-weight: bold;
  color: #303133;
}

.seat-price {
  color: #f56c6c;
  font-size: 18px;
  font-weight: bold;
}

.passenger-info {
  margin-top: 20px;
}

.passenger-info h3 {
  margin-bottom: 15px;
}

.submit-section {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 20px;
}

.price-summary {
  font-size: 14px;
}

.price-summary .label {
  color: #606266;
}

.price-summary .total-price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
}

:deep(.el-steps) {
  margin: 20px 0;
}

.journey-info {
  margin: 15px 0;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.station-info {
  text-align: center;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.station-info.end {
  align-items: flex-end;
}

.station-info .time {
  font-size: 16px;
  color: #606266;
}

.station-info .station {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin: 8px 0;
}

.station-info .date {
  font-size: 14px;
  color: #909399;
}

.journey-duration {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 20px;
}

.arrow-icon {
  color: #409EFF;
  font-size: 24px;
  margin-bottom: 8px;
}

.duration {
  font-size: 14px;
  color: #909399;
  white-space: nowrap;
}
</style>
