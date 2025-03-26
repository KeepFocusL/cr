import http from '../http.js'

const MODULE_PREFIX = '/u'

/**
 * 获取课程列表
 * @param {Object} params 查询参数
 * @param {number} params.page 当前页码
 * @param {number} params.size 每页显示条数
 * @returns {Promise}
 */
export function listCourse(params = {}) {
  const { page = 1, size = 20, keyword } = params
  return http.get(MODULE_PREFIX + '/admin/course/list', {
    params: {
      page,
      size,
      keyword
    }
  })
}

export function saveCourse(data) {
  return http.post(MODULE_PREFIX + '/admin/course/save', data)
}

export function deleteCourse(ids) {
  return http.delete(MODULE_PREFIX + '/admin/course/delete', { data: ids })
}
