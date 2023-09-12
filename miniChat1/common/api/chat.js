class chat {
	constructor(arg) {
		this.url = arg.url
		this.isOnline = false
		this.socket = null
		// 获取当前用户相关信息
		let user = uni.getStorageSync('userInfo')
		let token = uni.getStorageSync('token')
		this.token = token ? token : false
		this.user = user ? JSON.parse(user) : {}
		// 初始化聊天对象
		this.TO = false

		// 连接和监听
		if (this.token) {
			this.connectSocket()
		}

	}
	// 连接socket
	connectSocket() {
		this.socket = uni.connectSocket({
			url: this.url + this.token,

			complete: () => {

			}
		})

		// 监听连接成功
		this.socket.onOpen(() => this.onOpen())
		// 监听接收信息
		this.socket.onMessage((res) => this.onMessage(res))
		// 监听断开
		this.socket.onClose(() => this.onClose())
		// 监听错误
		this.socket.onError(() => this.onError())
	}
	// 监听打开
	onOpen() {
		// 用户上线
		this.isOnline = true
		console.log('socket连接成功')
		this.isOpenReconnect = true
		// 获取用户离线消息
		//this.getMessage()
	}
	// 获取离线消息
	getMessage() {
		$H.post('/chat/getmessage')
	}
	// 监听关闭
	onClose() {
		// 用户下线
		this.isOnline = false
		this.socket = null
		// if (this.isOpenReconnect) {
		// 	this.reconnect()
		// }
		console.log('socket连接关闭')
	}
	// 监听连接错误
	onError() {
		// 用户下线
		this.isOnline = false
		this.socket = null
		// if (this.isOpenReconnect) {
		// 	this.reconnect()
		// }
		console.log('socket连接错误')
	}
	// 监听接收消息

	onMessage(data) {
		//console.log('监听接收消息data', data)
		let res = JSON.parse(data.data)
		console.log('监听接收消息-----', res)
		// 错误
		if (res.data.chat_type != null) {
			this.handleOnMessage(res.data)
		}
		// switch (res.msg){
		// 	case 'fail':
		// 	return uni.showToast({
		// 		title: res.data,
		// 		icon: 'none'
		// 	});
		// 		break;
		// 	case 'recall': // 撤回消息
		// 	this.handleOnRecall(res.data)
		// 		break;
		// 	case 'updateApplyList': // 新的好友申请
		// 	$store.dispatch('getApply');
		// 		break;
		// 	case 'moment': // 朋友圈更新
		// 	this.handleMoment(res.data)
		// 		break;
		// 	default:
		// 	// 处理消息
		// 	this.handleOnMessage(res.data)
		// 		break;
		// }


	}
	// 处理消息
	async handleOnMessage(message) {
		// 添加消息记录到本地存储中
		//message.chat_type = 'user'
		let {
			data
		} = this.addChatDetail(message, false)
		// 更新消息列表
		this.updateChatList(data, false)
		// 全局通知
		uni.$emit('onMessage', data)

	}

	// 关闭连接
	close() {
		this.socket.close()
	}

	// 创建聊天对象
	createChatObject(detail) {
		this.TO = detail
		console.log("创建聊天对象", this.TO)
		console.log("创建聊天对象的id", this.TO.id)
	}

	// 组织发送信息格式
	formatSendData(params) {
		return {
			id: 0, // 唯一id，后端生成，用于撤回指定消息
			from_avatar: this.user.avatar, // 发送者头像
			from_name: this.user.nickname || this.user.username, // 发送者昵称
			from_id: this.user.id, // 发送者id
			to_id: params.to_id || this.TO.id, // 接收人/群 id
			to_name: params.to_name || this.TO.name, // 接收人/群 名称
			to_avatar: params.to_avatar || this.TO.avatar, // 接收人/群 头像
			chat_type: params.chat_type || this.TO.chat_type, // 接收类型
			type: params.type, // 消息类型
			data: params.data, // 消息内容
			options: params.options ? params.options : {}, // 其他参数
			//create_time: params.time, // 创建时间
			isremove: 0, // 是否撤回
			sendStatus: params.sendStatus ? params.sendStatus : "pending" // 发送状态，success发送成功,fail发送失败,pending发送中
		}
	}

	// 销毁聊天对象
	destoryChatObject() {
		this.TO = false
		console.log("销毁聊天对象", this.TO)
	}

	// 断线重连提示
	reconnectConfirm() {
		//this.reconnectTime = 0
		uni.showModal({
			content: '你已经断线，是否重新连接？',
			confirmText: "重新连接",
			success: (res) => {
				if (res.confirm) {
					this.connectSocket()
				}
			}
		});
	}
	// 验证是否上线
	checkOnline() {
		if (!this.isOnline) {
			// 断线重连提示
			this.reconnectConfirm()
			return false
		}
		return true
	}
	// 添加聊天记录
	addChatDetail(message, isSend = true) {
		console.log("添加聊天记录", message)
		// 获取对方id
		// console.log("this.user",this.user)
		let id = message.chat_type === 'user' ? (isSend ? message.receiverId : message.senderId) : message
			.receiverId
		let key = `chatDetail_${this.user.id}_${message.chat_type}_${id}`
		// 获取原来的聊天记录
		let list = this.getChatDetail(key)

		console.log("获取原来的聊天记录", message)
		// 标识
		message.k = 'k' + list.length
		// 加入存储
		console.log("加入缓存", key)
		list.push(message)
		this.setStorage(key, list)
		// 返回
		return {
			data: message,
			k: message.k
		}
	}
	// 更新指定历史记录
	async updateChatDetail(message, k, isSend = true) {
		console.log("updateChatDetail更新指定历史记录", message)
		// 获取对方id
		let id = message.chat_type === 'user' ? (isSend ? message.receiverId : message.senderId) : message
			.receiverId
		// key值：chatDetail_当前用户id_会话类型_接收人/群id
		let key = `chatDetail_${this.user.id}_${message.chat_type}_${id}`
		console.log('新指定历史记录key值', key)
		// 获取原来的聊天记录
		let list = this.getChatDetail(key)
		//console.log('获取原来的聊天记录',list)
		// 根据k查找对应聊天记录
		//console.log('找到k',k)
		let index = list.findIndex(item => item.k === k)

		if (index === -1) return;
		list[index] = message
		//console.log('新指定历史记录list[index]值',list[index])
		// 存储
		this.setStorage(key, list)
	}

	// 获取聊天记录
	getChatDetail(key = false) {
		console.log("keyTO", this.TO)
		console.log("keyTO", this.TO.id)
		console.log("keychat_type", this.TO.chat_type)
		key = key ? key : `chatDetail_${this.user.id}_${this.TO.chat_type}_${this.TO.id}`
		console.log("key", key)
		return this.getStorage(key)
	}
	
	// 格式化会话最后一条消息显示
		formatChatItemData(message,isSend){
			let content = message.content
			switch (message.messageType){
				case 'emoji':
				content = '[表情]'
					break;
				case 'image':
				content = '[图片]'
					break;
				case 'audio':
				content = '[语音]'
					break;
				case 'video':
				content = '[视频]'
					break;
				case 'card':
				content = '[名片]'
					break;
			}
			content = isSend ? content : `${message.senderName}: ${content}`
			return content
		}
	
	
	
	// 更新会话列表
	updateChatList(message, isSend = true) {
		// 获取本地存储会话列表
		let list = this.getChatList();
		// 是否处于当前聊天中
		let isCurrentChat = false
		// 接收id
		let id = 0
		let avatar = ''
		let name = ''
		//判断私聊还是群聊
		if (message.chat_type === 'user') { //私聊
			// 聊天对象是否存在
			isCurrentChat = this.TO ? (isSend ? this.TO.id === message.receiverId : this.TO.id === message
				.senderId) : false
			// if (!this.TO) {
			// 	isCurrentChat = false
			// } else {
			// 	isCurrentChat = isSend ? this.TO.id === message.receiverId : this.TO.id === message.senderId
			// }
			id = isSend ? message.receiverId : message.senderId
			avatar = isSend ? message.receiverAvatar : message.senderAvatar
			name = isSend ? message.receiverName : message.senderName
		} else { //群聊
			isCurrentChat = this.TO && (this.TO.id === message.receiverId)
			id = message.receiverId
			avatar = message.receiverAvatar
			name = message.receiverName
		}

		// 判断会话是否存在
		let index = list.findIndex(item => {
			return item.chat_type === message.chat_type && item.id === id
		})
		// 最后一条消息展现senderName
		//let data = isSend ? message.content : `${message.senderName}: ${message.content}`
		let data=this.formatChatItemData(message,isSend)
		// 未读数是否 + 1
		let noreadnum = (isSend || isCurrentChat) ? 0 : 1
		// let noreadnum = (isSend && isCurrentChat) ? 0 : 1
		// 会话不存在,创建会话
		if (index === -1) {

			let chatItem = {
				id, // 接收人/群 id
				chat_type: message.chat_type, // 接收类型 user单聊 group群聊
				avatar, // 接收人/群 头像
				name, // 接收人/群 昵称
				update_time: (new Date()).getTime(), // 最后一条消息的时间戳
				data, // 最后一条消息内容
				type: message.type, // 最后一条消息类型
				noreadnum, // 未读数
				sessionId: message.sessionId,
				istop: false, // 是否置顶
				shownickname: false, // 是否显示昵称
				nowarn: false, // 消息免打扰
				strongwarn: false, // 是否开启强提醒
			}
			// 群聊
			if (message.chat_type === 'group') {
				chatItem.shownickname = true
				chatItem.name = message.receiverName
				chatItem = {
					...chatItem,

					user_id: message.owner_id, // 群管理员id
					remark: "", // 群公告
					invite_confirm: 1, // 邀请确认
				}
			}
			list.unshift(chatItem)
		} else { //存在，更新会话
			let item = list[index]
			// 更新该会话的最后一条消息
			item.update_time = (new Date()).getTime()
			item.name = message.receiverName
			item.data = data
			item.type = message.messageType
			// 未读数加一
			item.noreadnum += noreadnum
			// 置顶会话
			list = this.listToFirst(list, index)
		}

		// 存储
		let key = `chatlist_${this.user.id}`
		this.setStorage(key, list)
		// 更新角标未读数
		//this.updateBadge(list)
		// 通知更新vuex中的聊天会话列表
		uni.$emit('onUpdateChatList', list)
		return list

	}

	// 更新未读数
	async updateBadge(list = false) {
		// 获取所有会话列表
		list = list ? list : this.getChatList()
		// 统计所有未读数
		let total = 0
		list.forEach(item => {
			total += item.noreadnum
		})
		// 设置底部导航栏角标
		// if (total > 0) {
		// 	uni.setTabBarBadge({
		// 		index: 0,
		// 		text: total <= 99 ? total.toString() : '99+'
		// 	})
		// } else {
		// 	uni.removeTabBarBadge({
		// 		index: 0
		// 	})
		// }
		uni.$emit('totalNoreadnum', total)
	}


	// 获取本地存储会话列表
	getChatList() {
		let key = `chatlist_${this.user.id}`
		// 获取本地存储
		return this.getStorage(key)

	}


	// 读取会话
	async readChatItem(id, chat_type) {
		// 获取所有的会话列表
		let list = this.getChatList()
		// 找到当前会话
		let index = list.findIndex(item => item.id === id && item.chat_type === chat_type)
		if (index !== -1) {
			list[index].noreadnum = 0
			let key = `chatlist_${this.user.id}`
			this.setStorage(key, list)
			// 重新获取总未读数
			this.updateBadge()
			// 更新会话列表
			uni.$emit('onUpdateChatList', list)
		}

	}

	// 获取存储
	getStorage(key) {

		let list = uni.getStorageSync(key)
		return list ? JSON.parse(list) : []
	}
	// 设置存储
	setStorage(key, value) {

		return uni.setStorageSync(key, JSON.stringify(value))
	}

	// 数组置顶
	listToFirst(arr, index) {
		if (index != 0) {
			arr.unshift(arr.splice(index, 1)[0]);
		}
		return arr;
	}

}

export default chat