import request from '@/utils/request'

//获取会员列表
export function fetchUser(query) {
  return request({
    url: '/admin/user/getlist',
    method: 'get',
    params: query
  })
}

//更新会员信息
export function updateuser(data) {
  return request({
    url: '/admin/user/updatelist',
    method: 'post',
    data
  })
}

//上传头像
export function sendAvatar(picfile) {
  return request({
    url: '/admin/user/fileUpload',
    method: 'post',
    data:picfile,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除用户
 * @param data
 * @returns {AxiosPromise}
 */
export function deleteuser(data) {
  return request({
    url: '/admin/user/deleteUser',
    method: 'post',
    data
  })
}

/**
 * 获取用户收藏歌曲
 * @param query
 * @returns {AxiosPromise}
 */
export function fetchUserSong(query) {
  return request({
    url: '/user/getUserSong',
    method: 'get',
    params: query
  })
}
