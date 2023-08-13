<template>
	<view>
		<!-- 导航栏 -->
		<free-nav-bar :title="detail.name" :showBack="true"></free-nav-bar>

		<!-- 聊天区域何 -->
		<scroll-view scroll-y="true" class="bg-hover-light position-fixed left-0 right-0 px-3" style="bottom: 105rpx;"
			:style="chatBodyBottom" :show-scrollbar="false">
			<!-- 聊天信息列表 -->
			<block v-for="(item,index) in list" :key="index">
				<free-chat-item :item="item" :index="index" :pretime="index>0?list[index-1].createTime:0" @long="long"
					@preview="previewImage" ref="chatItem"></free-chat-item>
			</block>
		</scroll-view>

		<!-- 点击不属于该区域的位置关闭弹出 -->
		<div v-if="mode==='action'||mode==='text'||mode==='emoji'" class="position-fixed top-0 right-0 bottom-0
		 left-0" :style="'bottom:'+maskBottom+'px;'" @click="clickpage">
		</div>

		<!-- 底部输入框 -->
		<view class="position-fixed left-0 right-0 
		border-top flex align-center" style="height: 105rpx; background-color: #F7F7F6"
			:style="'bottom:'+KeyboardHeight+'px'">

			<view class="flex-1">
				<input fixed class="bg-white rounded p-1 font-md ml-3" v-model="text" style="height: 80rpx;"
					:adjust-position="false" @focus="InputFocus" />

			</view>
			<!-- 表情 -->
			<free-icon-button :icon="'\ue6af'" @click="openActionOrEmoji('emoji')">

			</free-icon-button>
			<!-- 扩展 -->
			<template v-if="text.length===0">
				<free-icon-button :icon="'\ue65c'" @click="openActionOrEmoji('action')">

				</free-icon-button>
			</template>
			<template v-else>
				<!-- 发送 -->
				<view class="rounded mr-2 px-2 py-1" hover-class="bg-hover-light" style="background-color:#00CFFD ;"
					@click="send('text')">
					<text class="font text-white">发送</text>
				</view>
			</template>

		</view>
		<!-- 拓展菜单弹出 -->
		<free-popup ref="action" bottom transformOrigin="center bottom" @hide="KeyboardHeight=0" :mask="false">
			<view style="height: 280rpx;" class="border-top border-light-secondary bg-light">
				<swiper style="height: 280rpx;">
					<swiper-item class="row align-center" v-for="(item,index) 
					in emojiOrActionList" :key="index">
						<template v-if="mode==='action'">
							<view class="col-3 flex flex-column align-center bg-hover-light
					justify-center" v-for="(item2,index2) in item" :key="index2" style="height: 200rpx;" @click="actionEvent(item2)">
								<image :src="item2.icon" mode="widthFix" style="width: 60rpx;height: 60rpx;"></image>
								<text class="font text-light-muted mt-2">{{item2.name}}</text>
							</view>
						</template>
						<template v-else>
							<!-- 表情包 -->
							<view class="flex">

								<view class="col-2 flex-column justify-center align-center 
							" v-for="(item2,index2) in item" :key="index2" style="height: 200rpx;" @click="actionEvent(item2)">
									<image :src="item2.icon" mode="widthFix" style="width: 50rpx;height: 50rpx;">
									</image>

								</view>
							</view>

						</template>

						<!-- <image src="/static/emoji/face-open.png"></image> -->
					</swiper-item>

				</swiper>

			</view>
		</free-popup>
		<!-- 长按消息弹出层 -->
		<free-popup ref="extend" :bodyWidth="240" :bodyHeight="getMenusHeight" :tabbarHeight="105">
			<view class="flex flex-column" style="width: 240rpx;" :style="getMenusStyle">
				<view class="flex-1 flex align-center" hover-class="bg-light" v-for="(item,index) in menusList"
					:key="index">
					<text class="font-md pl-3">{{item.name}}</text>
				</view>
			</view>
		</free-popup>

	</view>
</template>

<script>
	// #ifdef APP-PLUS-NVUE
	const dom = weex.requireModule('dom')
	// #endif

	import freeIconButton from '@/components/free-ui/free-icon-button.vue'
	import freeNavBar from '@/components/free-ui/free-nav-bar.vue'
	import freeChatItem from '@/components/free-ui/free-chat-item.vue';
	import freePopup from '@/components/free-ui/free-popup.vue';
	import time from '@/common/time';

	import user from '../../store/user';
	import $H from '@/common/request.js';
	import {
		mapState
	} from 'vuex';

	export default {
		components: {
			freeIconButton,
			freeNavBar,
			freeChatItem,
			freePopup
		},
		data() {
			return {
				statusBarHeight: 0,
				navBarHeight: 0,
				propIndex: -1,
				// 键盘高度
				KeyboardHeight: 0,
				// 输入模式text文字输入、emoji表情、action操作、audio音频
				mode: "",
				// 输入文字
				text: "",
				// 扩展菜单列表
				actionList: [
					[{
						name: "相册",
						icon: "/static/images/extends/pic.png",
						event: "uploadImage"
					}, {
						name: "拍摄",
						icon: "/static/images/extends/camera.png",
						event: "uploadVideo"
					}, {
						name: "收藏",
						icon: "/static/images/extends/box.png",
						event: "openFava"
					}, {
						name: "名片",
						icon: "/static/images/extends/man.png",
						event: "sendCard"
					}]
				],
				emojiList: [
					[{

							icon: "/static/emoji/face-open.png",
							event: "sendemoji"
						}, {
							icon: "/static/emoji/face-hearts.png",
							event: "sendemoji"
						}, {
							icon: "/static/emoji/face-laughing.png",
							event: "sendemoji"
						}, {
							icon: "/static/emoji/face-sunglasses.png",
							event: "sendemoji"
						},
						{
							icon: "/static/emoji/face-crying.png",
							event: "sendemoji"
						},
						{
							icon: "/static/emoji/face-pleading.png",
							event: "sendemoji"
						}
					]
				],
				menus: [{
						name: "复制",
						event: "setTop"
					}, {
						name: "发送给朋友",
						event: "setTop"
					}, {
						name: "收藏",
						event: "setTop"
					}, {
						name: "撤回",
						event: "setTop"
					},
					{
						name: "删除",
						event: "delChat"
					}
				],
				list: [],
				// list: [{
				// 		avatar: '/static/images/defaultBgImg.png',
				// 		user_id: 1,
				// 		nickname: '昵称',
				// 		type: 'text',
				// 		data: '江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水',
				// 		create_time: 1689650855
				// 	},
				// 	{
				// 		avatar: '/static/images/defaultBgImg.png',
				// 		user_id: 2,
				// 		nickname: '昵称',
				// 		type: 'text',
				// 		data: 'uuu江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水',
				// 		create_time: 1689670455
				// 	},
				// 	{
				// 		avatar: '/static/images/defaultBgImg.png',
				// 		user_id: 2,
				// 		nickname: '昵称',
				// 		type: 'text',
				// 		data: 'uuu江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水',
				// 		create_time: 1689670455
				// 	},
				// 	{
				// 		avatar: '/static/images/defaultBgImg.png',
				// 		user_id: 2,
				// 		nickname: '昵称',
				// 		type: 'text',
				// 		data: 'uuu江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水',
				// 		create_time: 1689670755
				// 	}, {
				// 		avatar: '/static/images/defaultBgImg.png',
				// 		user_id: 2,
				// 		nickname: '昵称',
				// 		type: 'text',
				// 		data: 'uuu江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水',
				// 		create_time: 1689673423
				// 	},
				// 	{
				// 		avatar: '/static/images/defaultBgImg.png',
				// 		user_id: 1,
				// 		nickname: '昵称',
				// 		type: 'text',
				// 		data: 'uuu江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水江河夜色，淼淼炊烟，夜色如水',
				// 		create_time: 1689673423
				// 	},
				// 	{
				// 		avatar: '/static/images/defaultBgImg.png',
				// 		user_id: 1,
				// 		nickname: '昵称',
				// 		type: 'video',
				// 		data: '/static/video/demo.mp4',
				// 		options: {
				// 			poster: '/static/video/demo.jpg'
				// 		},
				// 		create_time: 1690354379
				// 	},
				// ],
				// 好友信息
				detail: {
					id: 0,
					name: "",
					avatar: "",
					session_id: "",
					chat_type: "user"
				}
			}
		},
		onLoad(e) {
			console.log("friend_id,sessionid", e)
			// 获取好友信息
			if (!e.friend_id) {
				uni.navigateBack({
					delta: 1
				})
				return uni.showToast({
					title: '没有该好友'
				})
			}
			let friend_id = e.friend_id
			let chat_type = e.type
			this.detail.session_id = e.session_id
			// 好友信息this.detail
			this.detail.id = friend_id
			this.detail.chat_type = chat_type
			this.getFriendInfo(friend_id, chat_type)
			// // 创建聊天对象
			this.chat.createChatObject(this.detail)
			// 获取历史记录
			let list = this.chat.getChatDetail(false)
			//console.log("====", list)
			this.list = list
			// 监听接收聊天信息
			uni.$on('onMessage',(message)=>{
				console.log("聊天页接收消息", message)
				if (message.senderId===this.detail.id
				&&message.chat_type===this.detail.chat_type) {
					this.list.push(message)
				}
			})

			this.statusBarHeight = 0
			// #ifdef APP-PLUS-NVUE
			this.statusBarHeight = plus.navigator.getStatusbarHeight()
			console.log("navBarHeight这个给", this.statusBarHeight)
			// #endif

			this.navBarHeight = this.statusBarHeight + uni.upx2px(90)
			console.log("这个给", this.navBarHeight)
			// this.pageToBottom()


		},
		destroyed() {
			// 摧毁聊天对象
			this.chat.destoryChatObject()
			// 摧毁监听接收聊天消息
			uni.$off('onMessage',()=>{})
		},
		mounted() {

			// 监听键盘高度变化
			uni.onKeyboardHeightChange(res => {
				setTimeout(() => {
					if (this.mode !== 'action' && this.mode !== 'emoji') {
						this.KeyboardHeight = res.height
					}
					if (this.KeyboardHeight > 0) {
						this.pageToBottom()
					}
				}, 100);


			});
			this.$nextTick(() => {
				this.pageToBottom();
			});

		},
		computed: {
			// 聊天对象
			...mapState({
				chat: state => state.user.chat,
				user: state => state.user.user,
			}),


			// 蒙版底部高度
			maskBottom() {
				return this.KeyboardHeight + uni.upx2px(105)
			},
			// 动态获取菜单高度
			getMenusHeight() {
				let H = 90
				return this.menus.length * H
			},
			// 获取菜单的样式
			getMenusStyle() {
				return `height: ${this.getMenusHeight}rpx;`
			},
			// 判断是否本人长按操作
			isdoSelf() {
				let id = 1
				let user_id = this.propIndex > -1 ? this.list[this.propIndex].user_id : 0
				return user_id === id
			},
			// 获取操作菜单
			menusList() {
				return this.menus.filter(v => {
					if (v.name === '撤回' && !this.isdoSelf) {
						return false
					} else {
						return true
					}
				})
			},
			// 聊天区域bottom
			chatBodyBottom() {

				return `bottom: ${this.KeyboardHeight + uni.upx2px(105)}px;top:${this.navBarHeight}px;`
			},
			// 获取操作或者表情列表
			emojiOrActionList() {
				return (this.mode === 'emoji' || this.mode === 'action') ? this[this.mode + 'List'] : []
			},
			// 所有信息图片地址加载进数组
			imageList() {
				let arr = []
				this.list.forEach((item) => {
					if (item.type === 'image') {
						arr.push(item.data)
					}
				})
				return arr
			}
		},
		watch: {
			mode(newValue, oldValue) {
				if (newValue !== 'action' && newValue !== 'emoji') {
					this.$refs.action.hide()
				}
				if (newValue !== 'text') {
					uni.hideKeyboard()
				}
			}
		},
		methods: {
			// 获取好友信息

			getFriendInfo(friend_id, chat_type) {
				$H.post('/ddchat/user/info', {
					friend_id: friend_id
				}).then(res => {
					console.log("好友信息", res)
					//this.detail.id = friend_id
					this.detail.avatar = res.avatar
					this.detail.name = res.nickname
					// id: 0,
					// name: "",

					// chat_type: "user"
				})
			},



			// 输入聚焦
			InputFocus() {
				this.mode = 'text'
			},

			// 打开扩展菜单或表情包
			openActionOrEmoji(mode = '') {
				this.mode = mode
				this.$refs.action.show()
				uni.hideKeyboard()
				this.KeyboardHeight = uni.upx2px(280)
				this.pageToBottom()
			},
			// 发送消息
			send(type, data = '') {
				// 组织数据格式
				//var time = (new Date()).getTime()
				switch (type) {
					case 'text':
						data = this.text
						break;
						// 	default:
						// 		obj.data = data
						// 		break;
				}
				//this.chat.formatSendData(params)
				// 渲染到页面

				// 发送到服务端
				let m = {
					senderId: this.user.id,
					senderAvatar: this.user.avatar,
					senderName: this.user.nickname,
					receiverId: this.detail.id,
					receiverAvatar: this.detail.avatar,
					receiverName: this.detail.name,
					sendTime: time.getFormatTime(new Date()),
					createTime: (new Date()).getTime(),
					content: this.text,
					sendStatus: "",
					chat_type: this.detail.chat_type,
					messageType: type,
					sessionId: this.detail.session_id
				}
				// 添加消息历史记录
				let {
					k
				} = this.chat.addChatDetail(m);

				// 更新会话列表

				// 更新会话列表
				this.chat.updateChatList(m)
				// 验证是否上线
				if (!this.chat.checkOnline()) return reject('未上线')

				this.list.push(m)
				//console.log("updateChatDetail的k",k)
				this.chat.updateChatDetail(m, k)
				let msg = {
					type: 'person-message',
					data: m
				}
				console.log("发送的---msg", msg)
				//let _this = this
				uni.sendSocketMessage({
					data: JSON.stringify(msg)

				}).then(res => {
					// 发送成功
					// message.id = res.id
					console.log("发送成功")
					m.sendStatus = 'success'
					uni.showToast({
						title: "成功了",
						icon: 'success'
					})
					// 更新指定历史记录
					console.log('更新指定历史记录eeee', m);
					this.chat.updateChatDetail(m, k)
					// result(res)
				}).catch(err => {
					// 发送失败
					m.sendStatus = 'fail'
					uni.showToast({
						title: "发送失败",
						icon: 'fail'
					})
					// 更新指定历史记录

					this.updateChatDetail(m, k)
					// 断线重连提示
					reject(err)
				})

				// 发送文字成功，清空输入框
				if (type === 'text') {
					this.text = ''
				}




				// var time = (new Date()).getTime()
				// var obj = {
				// 	avatar: '/static/images/defaultBgImg.png',
				// 	user_id: 1,
				// 	nickname: '昵称',
				// 	type: type, //image,audio,video,emoji
				// 	data: "",
				// 	options: {
				// 		poster: '/static/video/demo.jpg'
				// 	},
				// 	create_time: (new Date()).getTime()
				// }
				// switch (type) {
				// 	case 'text':
				// 		obj.data = this.text
				// 		break;
				// 	default:
				// 		obj.data = data
				// 		break;
				// }

				// this.list.push(obj)
				// // 发送文字成功，清空输入框
				// if (type === 'text') {
				// 	this.text = ''
				// }
				// // 置于底部
				// this.pageToBottom()
			},
			// 长按消息气泡
			long({
				x,
				y,
				index
			}) {
				// 初始化 索引
				this.propIndex = index

				this.$refs.extend.show(x, y)
			},
			// 回到底部
			pageToBottom() {
				setTimeout(() => {
					// #ifdef APP-PLUS-NVUE
					let chatItem = this.$refs.chatItem

					let lastIndex = chatItem.length > 0 ? chatItem.length - 1 : 0
					// console.log("lastIndex", lastIndex)
					if (chatItem[lastIndex]) {
						dom.scrollToElement(chatItem[lastIndex], {})
					}

					// #endif
				}, 310);

			},
			// 点击页面
			clickpage() {
				this.mode = ''
			},
			actionEvent(e) {
				switch (e.event) {
					case 'uploadImage':
						uni.chooseImage({
							count: 5,
							success: (res) => {
								// 发送后端接口
								// 渲染到页面
								res.tempFilePaths.forEach((item) => {
									this.send('image', item)
								})
							}
						})
						break;
					case 'uploadVideo': //发送短视频
						uni.chooseVideo({
							maxDuration: 10,
							success: (res) => {
								// 渲染页面
								this.send('video', res.tempFilePath)
								// 发送到服务端（获取封面，返回url）
								// 修改本地的发送状态
							}
						})

						break;
					case 'sendemoji':
						this.send('emoji', e.icon)
						break;
						// default:
						// break;
				}
			},
			// 点击预览图片
			previewImage(url) {
				uni.previewImage({
					current: url,
					urls: this.imageList
				})
			}

		}
	}
</script>


<style>

</style>