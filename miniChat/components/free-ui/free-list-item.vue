<template>
	<view class="bg-white flex align-stretch" hover-class="bg-light"
	@click="$emit('click')">
		<view class="flex align-center justify-center py-2 px-3">
			<slot name="icon"></slot>
			<image :src="cover" v-if="cover"
			mode="widthFix" style="width: 75rpx;height: 75rpx;"></image>
		</view>
		<view class="flex-1 border-bottom flex align-center justify-between pr-3">
			<text class="font-md text-dark">{{title}}</text>
			<view class="flex align-center" v-if="showRight">
				<slot name="right"></slot>
				<!-- 右箭头 -->
				<text class="iconfont text-light-muted font-md">&#xe60c;</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			// 封面
			cover: {
				type: String,
				default: ""
			},
			// 标题
			title:{
				type:String,
				default:""
			},
			// 显示右边
			showRight:{
				type:Boolean,
				default:false
			}
		},
	}
</script>

<style>
</style>
