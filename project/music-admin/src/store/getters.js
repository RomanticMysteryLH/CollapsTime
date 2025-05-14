const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  roles: state => state.user.roles,
  token: state => state.user.token,
}
export default getters
