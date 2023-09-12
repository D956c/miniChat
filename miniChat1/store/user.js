import $C from '@/common/api/config.js';
import Chat from '@/common/api/chat.js';
import $H from '@/common/request.js';
export default {
	state: {
		user: false,
		token: false,
		// 通讯录列表
		mailList: [],
		// websocket
		chat: null,
		// 会话列表
		chatList: []
	},
	actions: {
		// 登录后的处理
		login({
			state,
			dispatch
		}, user) {
			// 存到状态
			state.user = user.userInfo
			state.token = user.token
			// 存储到本地存储中
			uni.setStorageSync('token', user.token)
			uni.setStorageSync('userInfo', JSON.stringify(user.userInfo))
			state.chat = new Chat({
				url: $C.socketUrl
			})
			// 获取会话列表
			dispatch('getChatList')


		},
		// 初始化登录状态
		initLogin({
			state,
			dispatch
		}) {
			let user = uni.getStorageSync('userInfo')
			let token = uni.getStorageSync('token')
			if (user) {
				state.user = JSON.parse(user)
				state.token = token
				//console.log("initLogin的usre", state.user)
				// 连接socket
				state.chat = new Chat({
					url: $C.socketUrl
				})
				// 获取会话列表
				//dispatchEvent('getChatList')
				dispatch('getChatList')
				//console.log("dispatch('getChatList')", state.chatList)
			}
		},
		// 获取通讯录列表
		getMailList({
			state
		}) {
			$H.post('/ddchat/friend/getList', {
				myId: state.user.id
			}).then(res => {
				//console.log("通讯录列表res", res)
				state.mailList = res
				console.log("通讯录列表state.mailList", state.mailList)
			})

		},
		// 获取会话列表
		getChatList({
			state
		}) {
			state.chatList = state.chat.getChatList()
			// 监听会话列表变化
			uni.$on('onUpdateChatList', (list) => {
				state.chatList = list
			})
		}
	}
}