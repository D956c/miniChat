<template>
	<view class="page">
		<view class="flex flex-wrap py-2 bg-white ">
			<view v-for="(item,index) in list" :key="index" class="flex flex-column align-center justify-center mb-2"
				style="width: 150rpx;">
				<free-avater :src="item.avatar" size="100" type="rounded-circle"></free-avater>
				<text class=" font text-muted mt-1 text-ellipsis" style="max-width: 100rpx;">{{item.nickname}}</text>
			</view>
			<view class="flex flex-column align-center justify-center mb-5" style="width: 150rpx;">
				<view class="flex align-center justify-center border
				 rounded-circle " hover-class="bg-light" style="width: 100rpx;height: 100rpx;" @click="openMail">
					<text class="text-light-muted" style="font-size: 60rpx;">+</text>
				</view>

			</view>
		</view>
		<free-divider></free-divider>
		<free-list-item title="群名称" showRight :showLeftIcon="false" @click="updateName">
			<text slot="right" class="font text-muted">{{detail.name}}</text>
		</free-list-item>
		<free-list-item title="群介绍" showRight :showLeftIcon="false"></free-list-item>
		<free-list-item title="群公告" showRight :showLeftIcon="false"></free-list-item>
		<free-divider></free-divider>
		<free-list-item title="我的群昵称" showRight :showLeftIcon="false">
			<text slot="right" class="font text-muted">昵称</text>
		</free-list-item>
		<free-list-item title="设为顶置" showRight :showLeftIcon="false" :showRightIcon="false">
			<switch slot="right" color="#00C6F0" />
		</free-list-item>
		<free-list-item title="消息免打扰" showRight :showLeftIcon="false" :showRightIcon="false">
			<switch slot="right" color="#00C6F0" />
		</free-list-item>
		<free-divider></free-divider>
		<free-list-item title="举报" showRight :showLeftIcon="false"></free-list-item>
		<free-divider></free-divider>
		<view class="py-3 flex align-center justify-center bg-white rounded" hover-class="bg-light">
			<text class="font-md text-danger">退出群聊</text>
		</view>
		<view class="py-3 flex align-center justify-center bg-white rounded" hover-class="bg-light">
			<text class="font-md text-danger">{{user.id}}</text>
		</view>
		<!-- <view style="height: 200rpx;"></view> -->

		<free-confirm ref="confirm" :title="'修改群名称'">
			<input type="text" v-model="confirmText" class="border-bottom font-md" />
		</free-confirm>

	</view>
</template>

<script>
	import freeAvater from '@/components/free-ui/free-avater.vue';
	import freeDivider from '@/components/free-ui/free-divider.vue';
	import freeListItem from '@/components/free-ui/free-list-item.vue';
	import freeConfirm from '@/components/free-ui/free-confirm.vue';
	import $H from '@/common/request.js';
	import {
		mapState
	} from 'vuex'
	import time from '@/common/time';
	import user from '@/store/user';
	export default {
		components: {
			freeAvater,
			freeDivider,
			freeListItem,
			freeConfirm
		},

		data() {
			return {
				confirmType: "name",
				confirmText: "",
				list: [],
				nickname: "", // 我在本群的昵称
				detail: {
					id: 0, // 接收人/群 id
					//chat_type: 'user', // 接收类型 user单聊 group群聊
					avatar: '', // 接收人/群 头像
					name: '', // 接收人/群 昵称

					istop: false, // 是否置顶
					shownickname: false, // 是否显示昵称
					nowarn: false, // 消息免打扰
					strongwarn: false, // 是否开启强提醒

					admin_id: 0, // 群管理员id
					remark: "", // 群公告
					invite_confirm: 0, // 邀请确认
				}
			}
		},
		onLoad(e) {
			console.log("groupset从chat中拿到的群组信息", e)
			let data = JSON.parse(e.params)
			this.detail.id = data.group_id
			this.detail.name = data.group_name
			this.detail.avatar = data.group_avatar
			this.getGroupMember(data.group_id)
		},
		// onShow() {
		// 	this.getGroupMember(this.detail.id)
		// },
		computed: {
			...mapState({
				user: state => state.user.user,
			}),
		},
		methods: {
			getGroupMember(groupId) {
				$H.post('/ddchat/group/member', {
					id: groupId
				}).then(res => {
					console.log('获取成功到群成员信息', res);
					// 处理返回的数据
					this.list = res
					this.detail.admin_id = res[0].owner_id
					// console.log('获取成功到群成员信detail.admin_id息', this.detail.admin_id );
					// this.groupName = res.name
					// this.avatar = res.avatar
					// this.groupDescribe = res.introduction
				}).catch(err => {
					uni.showToast({
						title: '请检查网络连接',
						icon: 'error'
					})
				})
			},
			// 修改群名称
			updateName() {
				if (this.detail.admin_id == this.user.id) {
					this.confirmType = 'name'
					this.confirmText = this.detail.name
					this.$refs.confirm.show((close) => {
						if (this.confirmText == '') {
							return uni.showToast({
								title: '群名称不能为空',
								icon: 'none'
							});
						}


						$H.post('/ddchat/group/update', {
							id: this.detail.id,
							name: this.confirmText
						}).then(res => {
							console.log('修改完成', res);
							
							// 处理返回的数据
							
							this.detail.name = this.confirmText
							this.changeMessage()
							uni.showToast({
								title: '修改成功',
								icon: 'none'
							});
							close()
						this.tomessagepage()
						})



					})
				} else {
					uni.showToast({
						title: '没有修改权限',
						icon: 'none'
					});
				}

			},
			changeMessage() {
				let m = {
					messageType: 'system',
					senderId: this.user.id,
					senderName: this.user.nickname,
					groupId: this.detail.id,
					sendTime: time.getFormatTime(new Date()),
					createTime: (new Date()).getTime(),

					chat_type: 'group',
					//receiverId = this.detail.id;
					receiverId: this.detail.id,

					receiverAvatar: this.detail.avatar,
					receiverName: this.detail.name,
				}
				let msg = {
					type: 'change-group',
					data: m
				}
				console.log("修改群名的信息", msg)
				uni.sendSocketMessage({
					data: JSON.stringify(msg)
				}).then(res => {
					// uni.showToast({
					// 	title: '邀请成功',
					// 	icon: 'none'
					// });
					// uni.navigateBack({
					// 	delta: 1
					// });
				})

			},
			tomessagepage(){
				uni.navigateTo({
					url:'/pages/message/message'
				})
			},
			// 跳转到联系人列表选择
			openMail() {
				uni.navigateTo({

					url: '/pages/mail/mail?params=' + encodeURIComponent(JSON.stringify({
						group_id: this.detail.id,
						group_name: this.detail.name,
						group_avatar: this.detail.avatar
					}))
				})
			}

		}
	}
</script>

<style>

</style>