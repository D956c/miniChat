<template>
	<view class="position-relative">
		<video :src="url" controls autoplay style="width: 750rpx;" 
		:style="'height:'+windowHeight+'px;'"
		></video>
		<view class="position-absolute" style="top: 70rpx;left: 20rpx;" @click="back">
			<text class="iconfont text-white font-lg">&#xe616;</text>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				url: "",
				windowHeight: 500
			}
		},
		onLoad(e) {
			if (e.url === '') {
				this.back()
				return uni.showToast({
					title: '无效视频',
					icon: 'none'
				});
			}
			this.url = e.url
			// 动态获取高度
			let res = uni.getSystemInfoSync()
			this.windowHeight = res.windowHeight;

		},
		methods: {
			back() {
				uni.navigateBack({
					delta: 1
				});
			}
		}
	}
</script>

<style>

</style>