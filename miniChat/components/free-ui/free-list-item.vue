<template>
	<view class="bg-white flex align-stretch" hover-class="bg-light"
	@click="$emit('click')">
		<view class="flex align-center justify-center py-2 pl-3"
		v-if="showLeftIcon">
			<slot name="icon"></slot>
			<image :src="cover" v-if="cover"
			mode="widthFix" style="width: 75rpx;height: 75rpx;"></image>
		</view>
		
		<view class="flex-1 border-bottom flex align-center
		 justify-between pr-3 py-3 pl-3">
			<text class="font-md text-dark">{{title}}</text>
			<view class="flex align-center" v-if="showRight">
				<slot name="right"></slot>
				<!-- 右箭头 -->
				<text v-if="showRightIcon" class="iconfont text-light-muted font-md">&#xe60a;</text>
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
			},
			// 显示左边图标
			showLeftIcon:{
				type:Boolean,
				default:true
			},
			// 是否显示右箭头
			showRightIcon:{
				type:Boolean,
				default:true
			},
		},
	}
</script>

<style>
</style>
