<template>
	<view class="">
		<free-transparent-bar :isFromIndex="isFromIndex"></free-transparent-bar>
		<view class="position-relative" style="height: 590rpx;background-color: aqua;">
			<image src="https://gd-hbimg.huaban.com/ca89bdf9c7876341731309c6a5a6f8d639f7ab022015f3-NxntaM_fw658webp"
				mode="aspectFill" style="height: 590rpx;" class="bg-secondary w-100"></image>
			<image :src="avatar" style="width: 120rpx;height: 120rpx;left: 30rpx;
			bottom: 45rpx;" class="rounded position-absolute"></image>
			<text class="text-white font-lg position-absolute" style="bottom: 85rpx;left: 160rpx;">{{groupName}}</text>
		</view>

		<view class="flex font-lg text-muted px-2 bg-white" style="margin-top: 50rpx;margin-bottom: 20rpx;">
			<text>群简介</text>
		</view>
		<view class="flex  p-2">
			<text class="long-text text-light-muted font" v-if="!groupDescribe">
				还没有群介绍哦！
			</text>
			<text class="long-text text-light-muted font" v-else>
				{{ groupDescribe }}
			</text>
		</view>
		<view class="flex" style="position: fixed; bottom: 20rpx;width: 100%;">
			<view class="rounded-circle p-3 flex align-center justify-center flex-1 bg-chat-item m-3"
				@click="createGroup">
				<text class="text-white font-md">进入星群</text>
			</view>
		</view>



	</view>
</template>

<script>
	import freeTransparentBar from '@/components/free-ui/free-transparent-bar.vue';
	export default {
		components: {
			freeTransparentBar
		},
		data() {
			return {
				groupDescribe: "",
				groupName: "",
				avatar: "",
				isFromIndex: false,
			}
		},
		onLoad(e) {
			console.log("====groupdetail", e)
			this.getGroupDetail(e.groupId)
			const pages = getCurrentPages();
			const prevPage = pages[pages.length - 2]; // 获取上一页
			if (prevPage && prevPage.route === 'pages/create-group/create-group') {
				// 从create-group进入该页面
				this.isFromIndex = true;
			} else {
				// 从其他页面进入该页面
				this.isFromIndex = false;
			}
		},
		methods: {
			getGroupDetail(groupId) {
				uni.request({
					url: 'http://localhost:9999/ddchat/group/info',
					method: 'POST',
					header: {
						'Content-Type': 'application/json'
					},
					data: {
						id: groupId,
					},
					success: (res) => {
						console.log('获取成功到群组信息', res.data);
						// 处理返回的数据
						this.groupName = res.data.data.name
						this.avatar = res.data.data.avatar
						this.groupDescribe = res.data.data.introduction
					},
					fail: (err) => {
						console.error('请求失败', err);
					}

				});

			}
		}
	}
</script>

<style>
	.long-text {
		word-wrap: break-word;
	}

	.page {
		background-color: #EDEDED;
		/* #ifndef APP-PLUS-NVUE */
		min-height: 100vh;
		height: auto;
		/* #endif */
		/* #ifdef APP-PLUS-NVUE */
		flex: 1;
		/* #endif */
	}
</style>