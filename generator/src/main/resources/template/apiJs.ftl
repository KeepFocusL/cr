import http from '../http.js'

const MODULE_PREFIX = '/${moduleFirstLetter}'

/**
 * 获取${tableNameCn}列表
 * @param {Object} params 查询参数
 * @param {number} params.page 当前页码
 * @param {number} params.size 每页显示条数
 * @returns {Promise}
 */
export function list${Domain}(params = {}) {
  const { page = 1, size = 20, keyword } = params
  return http.get(MODULE_PREFIX + '/<#if isAdmin>admin/</#if>${do_main}/list', {
    params: {
      page,
      size,
      keyword
    }
  })
}

<#if !readOnly>
export function save${Domain}(data) {
  return http.post(MODULE_PREFIX + '/<#if isAdmin>admin/</#if>${do_main}/save', data)
}

export function delete${Domain}(ids) {
  return http.delete(MODULE_PREFIX + '/<#if isAdmin>admin/</#if>${do_main}/delete', { data: ids })
}
</#if>
