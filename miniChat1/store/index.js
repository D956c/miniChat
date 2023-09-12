import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import user from '@/store/user.js';

export default new Vuex.Store({
	modules:{
		user
	},
	state: {
		loginStatus: false
	},
	mutations: {
		changeloginStatus(state) {
			state.loginStatus = true
		}
	}
})