import request from '@/utils/request'

/**
 * 获取歌单列表
 * @param query
 * @returns {AxiosPromise}
 */
export function fetchSongList(query) {
  return request({
    url: '/songList/songListPage',
    method: 'get',
    params: query
  })
}

/**
 * 获取歌单的所有歌曲
 * @param songListId,userId
 * @returns {AxiosPromise}
 */
export function fetchSongListSong(songListId,userId) {
  return request({
    url: '/songList/getsongListDetail',
    method: 'get',
    params: {songListId:songListId,userId:userId}
  })
}

/**
 * 添加歌单
 * @param data
 * @returns {AxiosPromise}
 */
export function addSongList(data) {
  return request({
    url: '/admin/songList/addSongList',
    method: 'post',
    data
  })
}

/**
 * 上传歌单封面
 * @param picfile
 * @returns {AxiosPromise}
 */

export function sendPic(picfile) {
  return request({
    url: '/admin/songList/songListPicUpload',
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
    url: '/admin/songList/songListFileDelete',
    method: 'post',
    params:{id:id,picfilePath:picPath}
  })
}

/**
 * 更新歌单信息
 * @param data
 * @returns {AxiosPromise}
 */
export function updateSongList(data) {
  return request({
    url: '/admin/songList/updateSongList',
    method: 'post',
    data
  })
}

/**
 * 删除歌单
 * @param data
 * @returns {AxiosPromise}
 */
export function deleteSongList(data) {
  return request({
    url: '/admin/songList/deleteSongList',
    method: 'post',
    data
  })
}

/**
 * 删除歌单评论
 * @param data
 * @returns {AxiosPromise}
 */
export function deleteSongListComment(data) {
  return request({
    url: '/admin/songList/deleteSongListComment',
    method: 'post',
    data
  })
}

/**
 * 删除所选歌手的歌曲
 * @param data
 * @returns {AxiosPromise}
 */
export function deleteSongListSong(songListId,songIdstr) {
  return request({
    url: '/admin/songList/deleteSongListSong',
    method: 'post',
    params:{songListId:songListId,songIdstr:songIdstr}
  })
}
