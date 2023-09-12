<template>
	<view class="">
		<!-- 名称 -->
		<view class="flex font-lg text-muted px-2" style="margin-top: 50rpx;margin-bottom: 60rpx;">
			<text>群聊名称</text>
			<text style="color: red;">*</text>
		</view>

		<view class="px-2">
			<input type="text" class="bg-light px-3 mb-3 font rounded-circle" placeholder="群名称" style="height: 100rpx; "
				v-model="groupName" />
		</view>
		<!-- 头像选择 -->
		<view class="flex font-lg text-muted px-2 " style="margin-top: 50rpx;margin-bottom: 60rpx;">
			<text>群头像</text>
			<text style="color: red;">*</text>
		</view>
		<view class="px-2">
			<view v-for="(item,index) in avatarList" :key="index" class="flex align-center justify-center
		 rounded" style="width: 210rpx;
		 height: 210rpx; border-radius: 10rpx;" @click="chooseImage">
				<image :src="item" class="rounded" style="width: 210rpx;height: 210rpx;"></image>
			</view>
			<view v-if="avatarList.length<1" class="flex align-center justify-center
		 bg-light rounded" style="width: 220rpx;
		 height: 220rpx;" @click="chooseImage">
				<text class="text-light-muted" style="font-size: 100rpx;">+</text>
			</view>
		</view>
		<!-- 简介 -->
		<view class="flex font-lg text-muted px-2" style="margin-top: 40rpx;margin-bottom: 30rpx;">
			<text>简介</text>

		</view>
		<view class="px-2">
			<view class="p-2 bg-light" style="margin: 10rpx;border-radius: 10rpx;">
				<textarea v-model="content" placeholder="介绍你的群,让它更受欢迎~" style="width: 100%;" />

			</view>
		</view>

		<view class="px-2 flex align-center justify-center pt-2">
			<view class="rounded p-3 flex align-center justify-center flex-1" :disabled="disabled"
				:class="disabled?'bg-chat-item':'bg-no-complete'" @click="createGroup">
				<text class="text-white font-md">创建</text>
			</view>
		</view>

	</view>
</template>

<script>
	import {
		mapState
	} from 'vuex'
	import user from '@/store/user.js';

	export default {

		data() {
			return {
				groupName: '',
				avatarList: [],
				content: '',
				groupId:''
			};
		},
		computed: {
			...mapState({
				user: state => state.user.user
			}),
			disabled() {
				if (this.groupName === '' || this.avatarList.length === 0) {
					return false
				}
				return true

			}
		},
		methods: {
			selectAvatar(index) {
				this.selectedAvatar = index;
			},
			chooseImage() {
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					success: (res) => {
						this.avatarList = [...res.tempFilePaths]
					}
				})
			},
			// 上传群头像
			createGroup() {
				uni.showLoading({
					title: '正在上传头像···',
				})
				uni.uploadFile({
					url: 'http://localhost:9999/ddchat/image/upload',
					filePath: this.avatarList[0],
					name: 'image',
					formData: {
						image: 'groupAvatar'
					},
					success: res => {
						uni.hideLoading()
						//console.log(res)
						let data = JSON.parse(res.data)
						console.log("上传的图片", data)
						this.createToGroup(data.data.url)
					},
					fail: err => {
						uni.hideLoading()
						console.log('请求失败__________', err);
					}
				});
			},


			createToGroup(url) {
				//console.log("=======useridnn", this.user.id)
				uni.request({
					url: 'http://localhost:9999/ddchat/group/create',
					method: 'POST',
					header: {
						'Content-Type': 'application/json'
					},
					data: {
						id: this.user.id,
						name: this.groupName,
						avatar: 'http://localhost:9999/' + url,
						introduction: this.content
					},
					success: (res) => {
					  console.log('请求成功', res.data);
					  // 处理返回的数据
					  let groupId = res.data.data.groupId;
					  console.log('请求成功groupId', groupId);
					  //this.domessage(groupId);
					  uni.navigateTo({
					
					  	url: '/pages/group-detail/group-detail?groupId='+groupId,
					  	success: res => {},
					  	fail: () => {},
					  	complete: () => {}
					  });
					},
					fail: (err) => {
					  console.error('请求失败', err);
					}

				// 		success: function(res) {
				// 			console.log('请求成功', res.data);
				// 			// 处理返回的数据
				// 			let groupId=res.data.data.groupId
				// 			console.log('请求成功groupId', groupId);
				// 			this.domessage(groupId)
				// 		},
				// 		fail: function(err) {
				// 			console.error('请求失败', err);
				// 		}
				});


			
			},
			
			domessage(groupId){
				// 发送到服务端
				let m = {
					userId: this.user.id,
					groupId: groupId,
				
				}
				
				
				
				
				let msg = {
					type: 'join-group',
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
					// uni.showToast({
					// 	title: "成功了",
					// 	icon: 'success'
					// })
					// 更新指定历史记录
					// console.log('更新指定历史记录eeee', m);
					//this.chat.updateChatDetail(m, k)
					// result(res)
				}).catch(err => {
					// 发送失败
					m.sendStatus = 'fail'
					uni.showToast({
						title: "发送失败",
						icon: 'fail'
					})
					// 更新指定历史记录
				
					//this.updateChatDetail(m, k)
					// 断线重连提示
					reject(err)
				})
			}
		}
	};
</script>

<style>

</style>