<template>
	<view>

		<!-- 导航栏 -->
		<!-- <free-nav-bar title="通讯录"></free-nav-bar> -->

		<!-- 通讯录列表 -->
		<free-list-item v-for="(item,i) in topList" :key="i" :title="item.title" :cover="item.cover"></free-list-item>

		<view class="flex bg-hover-light align-center justify-center">
			<text> 我的朋友</text>
		</view>
		<free-list-item v-for="(item2,index2) in list" :key="index2+ topList.length" :title="item2.notation"
			:cover="item2.avatar?item2.avatar:'/static/images/choose-popup/star4.png'"
			@click="openChat(item2.friend_id,item2.session_id)"></free-list-item>





	</view>
</template>

<script>
	import freeNavBar from "@/components/free-ui/free-nav-bar.vue"
	import freeListItem from "@/components/free-ui/free-list-item.vue"
	import {
		mapState
	} from 'vuex';
	export default {
		components: {
			freeNavBar,
			freeListItem
		},
		data() {
			return {
				topList: [{
						title: "新的朋友",
						cover: "/static/images/choose-popup/star5.png",
						event: ""
					},
					{
						title: "群聊",
						cover: "/static/images/choose-popup/star5.png",
						event: ""
					},
					{
						title: "标签",
						cover: "/static/images/choose-popup/star5.png",
						event: ""
					}
				],
				// list: []
			}
		},
		computed: {
			...mapState({
				list: state => state.user.mailList
			}),
			// buttonText() {
			// 	let text = '发送'
			// 	if (this.type === 'createGroup') {
			// 		text = '创建群组'
			// 	}
			// 	return text + ' (' + this.selectCount + ')'
			// },
			// modalTop() {
			// 	return (this.scrollHeight - uni.upx2px(150)) / 2
			// },
			// // 每个索引的高度
			// itemHeight() {
			// 	let count = this.list.length
			// 	if (count < 1) {
			// 		return 0
			// 	}
			// 	return this.scrollHeight / count


		},
		onLoad() {
			this.$store.dispatch('getMailList')
		},
		methods: {
			// 携带friend_id打开聊天
			openChat(friend_id, session_id) {
				uni.navigateTo({
					url: '/pages/chat/chat?friend_id=' + friend_id + '&session_id=' + session_id + '&type=user',
				})
			}
		}
	}
</script>

<style>

</style>