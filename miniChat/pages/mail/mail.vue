<template>
	<view>

		<!-- 导航栏 -->
		<free-nav-bar title="选择" showBack :showRight="true">
			<free-main-button :name="buttonText" slot="right" @click="submit"></free-main-button>
		</free-nav-bar>

		<!-- 通讯录列表 -->
		<scroll-view scroll-y="true" style="background-color: aqua;">

			<template v-if="type === ''">
				<free-list-item v-for="(item2,index2) in list" :key="index2" :title="item2.name"
					:cover="item2.avatar || '/static/images/userpic.png'" :showRightIcon="false" showRight
					@click="selectItem(item2)">
					<view slot="right" style="width: 40rpx;height: 40rpx;"
						class="border rounded-circle flex align-center justify-center mr-4">
						<view v-if="item2.checked" style="width: 30rpx;height: 30rpx;"
							class="bg-chat-item rounded-circle"></view>
					</view>
				</free-list-item>
			</template>




		</scroll-view>

		<!-- 侧边导航条 -->
		<!-- 	<view class="position-fixed right-0 bottom-0 bg-light flex flex-column" :style="'top:'+top+'px;'" style="width: 50rpx;" @touchstart="touchstart" @touchmove="touchmove" @touchend="touchend">
			<view class="flex-1 flex align-center justify-center"
			v-for="(item,index) in list" :key="index">
				<text class="font-sm text-muted">{{item.nickname}}</text>
			</view>
		</view> -->

		<!-- <view class="position-fixed rounded-circle bg-light border flex align-center justify-center" v-if="current"
			style="width: 150rpx;height: 150rpx;left: 300rpx;" :style="'top:'+modalTop+'px;'">
			<text class="font-lg">{{current}}</text>
		</view> -->

	</view>
</template>

<script>
	import freeNavBar from "@/components/free-ui/free-nav-bar.vue"
	import freeListItem from "@/components/free-ui/free-list-item.vue"
	import freeMainButton from '@/components/free-ui/free-main-button.vue';
	import {
		mapState
	} from 'vuex'
	import user from '@/store/user';
	import time from '@/common/time';
	import $H from '@/common/request.js';
	export default {
		components: {
			freeNavBar,
			freeListItem,
			freeMainButton
		},
		data() {
			return {
				typeIndex: 0,
				top: 0,
				scrollHeight: 0,
				scrollInto: '',
				current: '',

				selectList: [],

				type: "",
				groupName: "",
				groupAvatar: "",
				limit: 1,

				id: 0
			}
		},
		onLoad(e) {
			console.log("mail的加载e", e)
			let data = JSON.parse(e.params)
			this.id = data.group_id
			this.groupName = data.group_name
			this.groupAvatar = data.group_avatar
			// -----------
			let res = uni.getSystemInfoSync()
			this.top = res.statusBarHeight + uni.upx2px(90)
			this.scrollHeight = res.windowHeight - this.top

			this.$store.dispatch('getMailList')
		},
		computed: {
			...mapState({
				list: state => state.user.mailList,
				user: state => state.user.user,
			}),
			buttonText() {
				let text = '发送'
				if (this.type === 'createGroup') {
					text = '创建群组'
				}
				return text + ' (' + this.selectCount + ')'
			},
			modalTop() {
				return (this.scrollHeight - uni.upx2px(150)) / 2
			},
			// 每个索引的高度
			itemHeight() {
				let count = this.list.length
				if (count < 1) {
					return 0
				}
				return this.scrollHeight / count
			},
			// 选中数量
			selectCount() {
				return this.selectList.length
			}
		},
		methods: {
			touchstart(e) {
				this.changeScrollInto(e)
			},
			touchmove(e) {
				this.changeScrollInto(e)
			},
			touchend(e) {
				this.current = ''
			},
			// 联动
			changeScrollInto(e) {
				let Y = e.touches[0].pageY
				// #ifdef MP
				Y = Y - this.top
				// #endif
				let index = Math.floor(Y / this.itemHeight)
				let item = this.list[index]
				if (item) {
					this.scrollInto = 'item-' + item.title
					this.current = item.title
				}
			},
			// 选中/取消选中
			selectItem(item) {
				if (!item.checked && this.selectCount === this.limit) {
					// 选中|限制选中数量
					return uni.showToast({
						title: '最多选中 ' + this.limit + ' 个',
						icon: 'none'
					});
				}
				item.checked = !item.checked
				if (item.checked) { // 选中
					this.selectList.push(item)
				} else { // 取消选中
					let index = this.selectList.findIndex(v => v === item)
					if (index > -1) {
						this.selectList.splice(index, 1)
					}
				}
			},
			submit() {
				console.log("邀请人的信息", this.user);
				//
				console.log("选择---", this.selectList);
				let m = {
					messageType: 'system',
					senderId: this.user.id,
					senderName: this.user.nickname,
					groupId: this.id,
					sendTime: time.getFormatTime(new Date()),
					createTime: (new Date()).getTime(),
					userId: this.selectList[0].friend_id,
					Inviter: this.user.nickname,
					chat_type: 'group',
					//receiverId = this.detail.id;
					receiverId: this.id,

					receiverAvatar: this.groupAvatar,
					receiverName: this.groupName,
				}
				let msg = {
					type: 'join-group',
					data: m
				}
				console.log("邀请加入群聊的信息", msg)
				uni.sendSocketMessage({
					data: JSON.stringify(msg)
				}).then(res => {
					uni.showToast({
						title: '邀请成功',
						icon: 'none'
					});
					uni.navigateBack({
						delta: 1
					});
				})

				// $H.post('/group/invite', {
				// 	id: this.id,
				// 	user_id: this.selectList[0].user_id
				// }).then(res => {
				// 	uni.showToast({
				// 		title: '邀请成功',
				// 		icon: 'none'
				// 	});
				// 	uni.navigateBack({
				// 		delta: 1
				// 	});
				// })


			}
		}
	}
</script>

<style>

</style>