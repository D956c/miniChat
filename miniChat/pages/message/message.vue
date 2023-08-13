<template>
	<view>
		<!-- 导航栏 -->
		<view class="bg-light">
			<!-- 状态栏 -->

			<view class="" :style="'height:'+statusBarHeight+'px'" style="">
				 
			</view>
			<view class=" flex align-center justify-between " style="height: 80rpx;">
				<!-- 中间 -->
				<view class=" flex flex-1 justify-center align-center" style="">
					<text class="" style="font-weight: 600;">消息列表</text>
					<text class="iconfont  ml-1 font-lg" @click="removeNoread" style="color: #6e6c6b;">&#xe61e;</text>
				</view>
				<!-- 右边  -->

				<view class="flex justify-center align-center 
					border" style="height: 90rpx;width: 90rpx;" @click="popupOptions">
					<text class="iconfont font-lg">&#xe602;</text>
				</view>

			</view>
		</view>
		<!-- 弹出框 -->
		<choose-popup :visible="visible" @customEvent="handleCustomEvent"></choose-popup>
		<template v-if="messages.length>0">


			<view class="flex flex-1 bg-chat-item justify-center">
				<image class="rounded-circle" :src="user.avatar" style="height: 100rpx;width: 100rpx;"></image>
				<view class="flex align-center justify-between ml-2">
					<text class="font-md">{{user.nickname}}</text>
				</view>
			</view>



			<!-- 消息列表 -->

			<uni-swipe-action>
				<block v-for="(item,index) in sortedMessages" :key="index">
					<template v-if="item.istop">
						<uni-swipe-action-item :right-options="options1" @click="swipeClick($event, item,index)">
							<msg-list :item="item" :index="index" :class="{'top-message': item.istop}"
								@click="openChat(item.id,item.sessionId,item.chat_type)"></msg-list>

						</uni-swipe-action-item>
					</template>
					<template v-else>
						<uni-swipe-action-item :right-options=" options" @click="swipeClick($event, item,index)">
							<msg-list :item="item" :index="index" :class="{'top-message': item.istop}"
								@click="openChat(item.id,item.sessionId,item.chat_type)"></msg-list>

						</uni-swipe-action-item>
					</template>

				</block>
			</uni-swipe-action>

		</template>
		<template v-else>
			<no-thing></no-thing>
		</template>


	</view>
</template>

<script>
	const demo = [
		// {
		// 	username: "旅行小蛙",
		// 	userpic: "/static/images/defult/ww.png",
		// 	updata_time: "1688266084",
		// 	data: "舞动就打架大家出哈哈哈哈哈哈事实上我草草草草的急急急",
		// 	noread: 30,
		// 	isTop: false
		// }, {
		// 	username: "旅行小蛙",
		// 	userpic: "/static/images/defult/ww.png",
		// 	updata_time: "1688266084",
		// 	data: "舞动就打架大家出哈哈哈哈哈哈事实上我草草草草的急急急",
		// 	noread: 30,
		// 	isTop: false
		// },
		// {
		// 	username: "旅行小蛙",
		// 	userpic: "/static/images/defult/ww.png",
		// 	updata_time: "1688266084",
		// 	data: "舞动就打架大家出哈哈哈哈哈哈事实上我草草草草的急急急",
		// 	noread: 30,
		// 	isTop: false
		// }, {
		// 	username: "旅行搜索",
		// 	userpic: "/static/images/defult/ww.png",
		// 	updata_time: "1688266084",
		// 	data: "舞动就打架大家出哈哈哈哈哈哈事实上我草草草草的急急急",
		// 	noread: 30,
		// 	isTop: false
		// }
	];
	import choosePopup from '@/components/common/my-popup/choose-popup.vue'
	import msgList from '@/components/msg/msg-list.vue'
	import noThing from '@/components/common/no-thing.vue'
	import uniSwipeAction from '../../components/uni-ui/uni-swipe-action/components/uni-swipe-action/uni-swipe-action.vue'
	import uniSwipeActionItem from '../../components/uni-ui/uni-swipe-action/components/uni-swipe-action-item/uni-swipe-action-item.vue'
	import auth from '@/common/mixin/auth.js'
	import {
		mapState
	} from 'vuex'
	import user from '../../store/user';
	export default {
		mixins: [auth],
		components: {
			choosePopup,
			msgList,
			noThing,
			uniSwipeAction,
			uniSwipeActionItem
		},
		data() {
			return {
				visible: false, // 控制下拉框显示隐藏
				statusBarHeight: 0,
				options: [{
						text: '置顶'
					},
					{
						text: '标记为已读',
						style: {
							backgroundColor: 'rgb(254,156,1)'
						}
					},
					{
						text: '删除',
						style: {
							backgroundColor: 'rgb(255,58,49)'
						}
					}
				],
				options1: [{
						text: '取消顶置',
						style: {
							backgroundColor: 'rgb(85, 0, 255)'
						}
					},
					{
						text: '标记为已读',
						style: {
							backgroundColor: 'rgb(254,156,1)'
						}
					},
					{
						text: '删除',
						style: {
							backgroundColor: 'rgb(255,58,49)'
						}
					}
				],
				//messages: []

			};
		},


		onLoad() {
			console.log("拿到个人信息messages", this.sortedMessages)
			//this.messages = demo;
			this.statusBarHeight = 0
			// #ifdef APP-PLUS-NVUE


			this.statusBarHeight = plus.navigator.getStatusbarHeight()
			// #endif
			// //const self = this;
			uni.$on('msg-popup', (data) => {
				this.visible = data.msg;
				console.log(data)
				console.log("信息", this.msg)
			});
			// const storedMessages = uni.getStorageSync('messages');
			// if (storedMessages) {
			//   this.messages = storedMessages;
			// }
		},

		onUnload() {
			//移除监听
			uni.$off('msg-popup')
		},
		onPullDownRefresh() {

			this.refresh()
		},
		// 监听原生导航按钮事件
		// onNavigationBarButtonTap(e) {
		// 	switch (e.index) {
		// 		// 左边清除所有未读消息
		// 		case 0:
		// 			for (let i = 0; i < this.messages.length; i++) {
		// 				this.messages[i].noread = 0; // 在循环中向数组list添加元素
		// 			};
		// 			break;
		// 			// 右边点击弹出
		// 		default:
		// 			1
		// 			this.visible = !this.visible;
		// 			break;
		// 	}
		// },

		// 顶置操作计算属性
		computed: {
			...mapState({
				user: state => state.user.user,
				messages: state => state.user.chatList,
				chat: state => state.user.chat
			}),
			sortedMessages() {
				const topMessages = this.messages.filter(message => message.istop);
				const normalMessages = this.messages.filter(message => !message.istop);
				return [...topMessages, ...normalMessages];
			}

		},
		mounted() {
			// 从本地存储中加载消息的置顶状态
			const storedMessages = uni.getStorageSync('messages');
			if (storedMessages) {
				this.messages = storedMessages;
			}
		},
		methods: {
			refresh() {
				this.messages = demo

				// 停止刷新
				uni.stopPullDownRefresh()
			},
			// 从组件choosePopup传过来的数据
			handleCustomEvent(data) {
				// 这里的data就是从子组件传递过来的数据
				console.log(data);
				// 在这里可以做进一步的处理
				switch (data) {
					case 'toUserList':
						// 跳转到user页面
						uni.navigateTo({
							url: '/pages/user-list/user-list' // user为你的用户页面路径
						});
						break;

					case 'toCreateGroup':
						// 跳转到qun页面
						uni.navigateTo({
							url: '/pages/create-group/create-group' // qun为你的群页面路径
						});
						break;

					default:
						// 处理其他情况
						break;
				}
			},

			// 携带friend_id打开聊天
			openChat(friend_id, session_id, chat_type) {
				console.log("點擊了去聊天")
				uni.navigateTo({
					url: '/pages/chat/chat?friend_id=' + friend_id + '&session_id=' + session_id + '&type=' +
						chat_type,
				})
				this.chat.readChatItem(friend_id, chat_type)
			},



			// console.log("点击了清除消息");
			removeNoread() {

				for (let i = 0; i < this.messages.length; i++) {
					this.messages[i].noread = 0; // 在循环中向数组list添加元素
				};
			},
			// console.log("点击了弹出"),
			popupOptions() {
				this.visible = !this.visible;
			},
			swipeClick(e, item, index) {
				console.log("点击了", e)
				console.log("点击index为", index)
				let {
					content
				} = e;
				console.log("点击content为", content)
				if (content.text === '删除') {
					uni.showModal({
						title: '提示',
						content: '是否删除',
						success: res => {
							if (res.confirm) {
								this.messages.splice(index, 1);
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					});
				} else if (content.text === '置顶' || content.text === '取消顶置') {
					item.istop = !item.istop;
					uni.setStorageSync('messages', this.messages)

				} else if (content.text === '标记为已读') {
					item.noread = 0;
				}
			},
		}

	}
</script>

<style>
	.top-message {
		background-color: lightcyan
	}

	.dd {
		background: linear-gradient(to right, #311bc6, #ff00ff, #12e8eb);
		/* 	 background: linear-gradient(to right, #ff00ff, #00ffff); */
		/* background: radial-gradient(circle, #ff00ff, #00ffff); */
		/* 		background: radial-gradient(circle, #311bc6, #0067f5, #0098ff, #00c2f7, #12e8eb); */
	}
</style>