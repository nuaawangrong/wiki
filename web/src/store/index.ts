import { createStore } from 'vuex'

declare let SessionStorage: any;
const USER = "USER";

const store = createStore({
  state: {
    user: SessionStorage.get(USER) || {}
  },
  mutations: {
    // 对变量的操作，同步
    setUser (state, user) {
      state.user = user;
      SessionStorage.set(USER, user);
    }
  },
  actions: {
    // 对变量的操作，异步
  },
  modules: {
  }
})

export default store;