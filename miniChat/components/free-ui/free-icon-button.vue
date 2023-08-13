<template>
	<view class="flex align-center justify-center" 
	hover-class="bg-hover-light" 
	style="height: 90rpx;width: 90rpx;" @click="$emit('click')">
		<text class="iconfont font-md">{{icon}}</text> 
			
	</view>
</template>

<script>
	export default {
		props: {
			icon: {
				type: String,
				default: ''
			},
		},
	}
</script>

<style>
</style>