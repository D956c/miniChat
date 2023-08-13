<template>
	<view>
		<view v-if="!show"
			class="position-fixed top-0 bottom-0 left-0 right-0 bg-light flex align-center justify-center">
			<text class="text-muted font">正在加载...</text>
		</view>

		<view v-else>
			<view class="flex align-center justify-center" style="height: 350rpx;">
				<text style="font-size: 50rpx;">YOU-LOGO</text>
			</view>
			<view class="px-3">
				<input type="text" class="bg-light px-3 mb-3 font" placeholder="请输入邮箱" style="height: 100rpx;"
					v-model="form.email" />
				<input type="text" class="bg-light px-3 mb-3 font" placeholder="请输入密码" style="height: 100rpx;"
					v-model="form.password" />
				<input v-if="type === 'register'" type="text" class="bg-light px-3 mb-3 font" placeholder="请输入确认密码"
					style="height: 100rpx;" v-model="form.repassword" />
			</view>
			<view class="p-3 flex align-center justify-center">
				<view class=" rounded p-3 flex align-center justify-center flex-1 bg-chat-item"
					hover-class="main-bg-hover-color" @click="Event_Login">
					<text class="text-white font-md">{{type === 'login' ? '登 录' : '注 册'}}</text>
				</view>
			</view>

			<view class="flex align-center justify-center">
				<text class="text-light-muted font p-2"
					@click="changeType">{{type === 'login' ?  '注册账号' : '马上登录'}}</text>
				<text class="text-light-muted font">|</text>
				<text class="text-light-muted font p-2">忘记密码</text>
			</view>
		</view>

	</view>
</template>

<script>
	import $H from '@/common/request.js';
	export default {
		data() {
			return {
				type: "login",
				show: false,

				form: {
					email: "",
					username: "",
					password: "",
					repassword: "",
				}
			}
		},
		created() {
			let token = uni.getStorageSync('token')
			//let token = uni.getStorage('token')
			if (!token) {
				// 用户未登录
				uni.showToast({
					title: '请先登录',
					icon: 'none'
				});
				return this.show = true
			}
			// 用户已登录
			uni.navigateTo({
				url: "/pages/message/message"
			})
		},
		onLoad() {
			// console.log(this.$store.state.loginStatus)
		},
		methods: {
			changeType() {
				this.type = this.type === 'login' ? 'register' : 'login'
				this.form = {
					email: "",
					username: "",
					password: "",
					repassword: "",
				}

			},
			submit() {
				console.log("执行submit")
				$H.post('/ddchat/index/login',
					this.form, {
						token: false
					}).then(res => {
					// 登录
					this.$store.dispatch('login', res)
					console.log(this.$store.dispatch('login', res))
					// console.log(res.token)
					// console.log(res.userInfo)
					uni.showToast({
						title: '登录成功！',
						icon: 'success'
					});
					uni.navigateTo({
						url: "/pages/message/message"
					})
				})

			},
			regsiter() {
				console.log("执行regsiter")
				$H.post('/ddchat/index/register', {
					email: this.form.email,
					nickname: "账号" + this.form.email,
					gender: "男",
					password1: this.form.password,
					password2: this.form.repassword,
				}, {
					token: false
				}).then(res => {

					uni.showToast({
						title: '注册成功！',
						icon: 'success'
					});

				})
			},
			Event_Login() {
				if (this.form.email && this.form.password) {
					if (this.type === 'login') {
						this.submit()
					} else {
						this.regsiter()
						this.changeType()
					}

				} else {
					uni.showToast({
						title: '请输入邮箱和密码',
						icon: "none",
						duration: 3000
					});
				}


			},
			// submit(){
			// 	if(this.type === 'login'){
			// 		//this.$store.dispatch('login',res)
			// 		uni.showToast({
			// 			title: '登录成功',
			// 			icon: 'none'
			// 		});
			// 		return uni.switchTab({
			// 			url:"/pages/message/message"
			// 		})
			// 	}
			// 	// $H.post('/'+this.type,this.form,{
			// 	// 	token:false
			// 	// }).then(res=>{
			// 	// 	// 登录
			// 	// 	if(this.type === 'login'){
			// 	// 		this.$store.dispatch('login',res)
			// 	// 		uni.showToast({
			// 	// 			title: '登录成功',
			// 	// 			icon: 'none'
			// 	// 		});
			// 	// 		return uni.switchTab({
			// 	// 			url:"/pages/tabbar/index/index"
			// 	// 		})
			// 	// 	}
			// 	// 	// 注册
			// 	// 	this.changeType()
			// 	// 	uni.showToast({
			// 	// 		title: '注册成功，去登陆',
			// 	// 		icon: 'none'
			// 	// 	});
			// 	// })
			// }
		}
	}
</script>

<style>

</style>