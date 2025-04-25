import http from '../http.js'

const MODULE_PREFIX = '/b'

/**
 * 获取余票信息列表
 * @param {Object} params 查询参数
 * @param {number} params.page 当前页码
 * @param {number} params.size 每页显示条数
 * @returns {Promise}
 */
export function listDailyTrainTicket(params = {}) {
  const { page = 1, size = 20, keyword } = params
  return http.get(MODULE_PREFIX + '/ticket/list', {
    params: {
      page,
      size,
      keyword
    }
  })
}

export function confirmOrder(data) {
  return http.post(MODULE_PREFIX + '/confirm-order/confirm', data)
}

