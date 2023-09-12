<template>
	<view>
		<view class="p-2nb">
			<!-- 头像昵称|功能按键 -->
			<view class="flex align-center  justify-between"
				style="flex-direction: row;">
				<view class="flex" style="flex-direction: row; align-items: center;">
					<image :src="item.userpic" @click="openSpace" style="height: 60rpx;
		width: 60rpx;border-radius: 100%;margin-right: 25rpx;" lazy-load></image>
					<view style="font-size: 30rpx;font-weight: 600;">{{item.username}}</view>
				</view>
				<!-- 拓展按键 -->
				<view>///</view>
			</view>
			<!-- 标题 -->
			<view  style="font-size: 30rpx;  margin-top: 20rpx; margin-bottom: 20rpx;" @click="openDetail">{{item.title}}</view>
		
		</view>
		<!-- 内容 -->
		<slot></slot>
		<!-- <carousel :img-list="item.imgList" url-key="url" @selected="selectedBanner"></carousel> -->
		<!-- 图标按钮 -->
		<view class="flex justify-center align-center" style="height: 95rpx;" >
			<view class="flex flex-1 justify-center align-center" hover-class="animate__animated animate__heartBeat text-main"
			:class="item.support.type === 'support'? 'text-main':''" @click="doSupport('support')" >
				<text class="iconfont icon-dianzan1" 
					style="font-size: 50rpx; margin-right: 20rpx;"></text>
				<text style="font-size: 25rpx;">{{item.support.support_count>0?item.support.support_count:'赞'}}</text>
			</view>
			<view class="flex flex-1 justify-center align-center" hover-class="animate__animated animate__heartBeat text-main"
			 :class="item.support.type === 'unsupport'? 'text-main':''" @click="doSupport('unsupport')">
				<text class="iconfont icon-cai" style="font-size: 50rpx;margin-right: 20rpx;"></text>
				<text style="font-size: 25rpx;">踩</text>
			</view>
			<view class="flex flex-1 justify-center align-center" @click="openDetail" >
				<text class="iconfont icon-pinglun1"
					style="font-size:50rpx;color: #000000;margin-right: 20rpx;"></text>
				<text style="font-size: 25rpx;">{{item.comment_count>0?item.comment_count:'评论'}}</text>
			</view>
			<view class="flex flex-1 justify-center align-center" >
				<text class="iconfont icon-fenxiang"
					style="font-size: 50rpx;color: #000000;margin-right: 20rpx;"></text>
				<text style="font-size: 25rpx;">分享</text>
			</view>
		</view>
	</view>
	
</template>

<script>
	export default {
		// name:"common-list",
		// data() {
		// 	return {
				
		// 	};
		props:{
			item:Object,
			index:Number
		},
		methods:{
			// 进入空间
			openSpace(){
				console.log("空间")
			},
			// 进入详情页
			openDetail(){
				console.log("详情页")
			},
			// 顶踩操作
			doSupport(type){
				// 通知父组件
				this.$emit('doSupport',{
					type:type,
					index:this.index
				})
				console.log(type)
			}
		}
		
	}
</script>

<style>

</style>