import request from '@/utils/request'

export function getCount(query) {
  return request({
    url: '/admin/home/getCount',
    method: 'get',
    params: query
  })
}

export function queryStasticOfHome(query) {
  return request({
    url: '/admin/home/queryStasticOfHome',
    method: 'get',
    params: query
  })
}

