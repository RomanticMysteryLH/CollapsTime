import request from '@/utils/request'

/**
 * 获取歌曲列表
 * @param query
 * @returns {AxiosPromise}
 */

export function fetchSong(query) {
  return request({
    url: '/admin/song/songPage',
    method: 'get',
    params: query
  })
}
