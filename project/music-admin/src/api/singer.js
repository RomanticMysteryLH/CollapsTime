import request from '@/utils/request'

/**
 * 获取歌手列表
 * @param query
 * @returns {AxiosPromise}
 */
export function fetchSinger(query) {
  return request({
    url: '/singer/singerPage',
    method: 'get',
    params: query
  })
}

/**
 * 获取歌手的所有歌曲
 * @param singerId,userId
 * @returns {AxiosPromise}
 */
export function fetchSingerSong(singerId,userId) {
  return request({
    url: '/singer/getSingerDetail',
    method: 'get',
    params: {singerId:singerId,userId:userId}
  })
}

/**
 * 添加歌手
 * @param data
 * @returns {AxiosPromise}
 */
export function addSinger(data) {
  return request({
    url: '/admin/singer/addSinger',
    method: 'post',
    data
  })
}

/**
 * 上传歌手照片
 * @param picfile
 * @returns {AxiosPromise}
 */

export function sendPic(picfile) {
  return request({
    url: '/admin/singer/singerPicUpload',
    method: 'post',
    data:picfile,
  })
}

/**
 * 删除多出来的图片文件
 * @param id
 * @param picPath
 * @returns {AxiosPromise}
 */
export function overridePic(id,picPath) {
  return request({
    url: '/admin/singer/singerFileDelete',
    method: 'post',
    params:{id:id,picfilePath:picPath}
  })
}

/**
* 更新歌手信息
* @param data
* @returns {AxiosPromise}
*/
export function updateSinger(data) {
  return request({
    url: '/admin/singer/updateSinger',
    method: 'post',
    data
  })
}

/**
 * 删除歌手
 * @param data
 * @returns {AxiosPromise}
 */
export function deleteSinger(data) {
  return request({
    url: '/admin/singer/deleteSinger',
    method: 'post',
    data
  })
}

/**
 * 删除所选歌手的歌曲
 * @param data
 * @returns {AxiosPromise}
 */
export function deleteSingerSong(data) {
  return request({
    url: '/admin/singer/deleteSingerSong',
    method: 'post',
    params:{idstr:data}
  })
}

/**
 * 搜索功能加获取歌手、歌曲、歌单列表
 * @param query
 * @returns {AxiosPromise}
 */
export function search(query) {
  return request({
    url: '/user/searchInfo',
    method: 'post',
    params:query
  })
}
