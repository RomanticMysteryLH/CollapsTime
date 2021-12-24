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

/**
 * 更新歌曲信息
 * @param data
 * @returns {AxiosPromise}
 */
export function updateSong(data) {
  return request({
    url: '/admin/song/updateSong',
    method: 'post',
    data
  })
}

/**
 * 上传歌曲封面
 * @param picfile
 * @returns {AxiosPromise}
 */

export function sendPic(picfile) {
  return request({
    url: '/admin/song/songPicUpload',
    method: 'post',
    data:picfile,
  })
}

/**
 * 上传Mp3
 * @param file
 * @returns {AxiosPromise}
 */
export function sendMp3(file) {
  return request({
    url: '/admin/song/songUpload',
    method: 'post',
    data:file,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除未保存但上传了的文件
 * @param path
 * @returns {AxiosPromise}
 */
export function deleteProFile(picPath,songPath) {
  return request({
    url: '/admin/song/songProFileDelete',
    method: 'post',
    params:{picfilePath:picPath,songPath:songPath}
  })
}

/**
 * 覆盖文件时删除原来的文件
 * @param id
 * @returns {AxiosPromise}
 */
export function deleteSongFile(id,select) {
  return request({
    url: '/admin/song/songFileDelete',
    method: 'post',
    params:{id:id,select:select}
  })
}

/**
 * 上传歌词
 * @param lrcfile
 * @returns {AxiosPromise}
 */
export function sendLrc(lrcfile) {
  return request({
    url: '/admin/song/songLrcUpload',
    method: 'post',
    data:lrcfile,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 添加歌曲
 * @param data
 * @returns {AxiosPromise}
 */
export function addSong(data) {
  return request({
    url: '/admin/song/addSong',
    method: 'post',
    data
  })
}

/**
 * 获取歌手列表
 * @param data
 * @returns {AxiosPromise}
 */
export function getSinger(data) {
  return request({
    url: '/admin/song/getSinger',
    method: 'get',
    params:data
  })
}

/**
 * 删除用户
 * @param data
 * @returns {AxiosPromise}
 */
export function deleteSong(data) {
  return request({
    url: '/admin/song/deleteSong',
    method: 'post',
    data
  })
}

