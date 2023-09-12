<script>
	export default {
		onLaunch: function() {
			// #ifdef APP-PLUS-NVUE
			// 加载公共图标库
			const domModule = weex.requireModule("dom");
			domModule.addRule('fontFace', {
				'fontFamily': 'iconfont',
				'src': "url('https://at.alicdn.com/t/c/font_4108146_cvh4tbap3u8.ttf')"
			});
			// #endif
			
			// 初始化登录状态
			this.$store.dispatch('initLogin')
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/* 图标 */
	/* 	@import url("https://at.alicdn.com/t/c/font_4108146_qlvp3s0hrrq.css");
 */
	/* 自定义样式 */
	@import 'common/free.css';
	/* 公共样式 */
	@import 'common/common.css';
	/* 动画库 */
	/* @import 'common/animate.css'; */

	/*每个页面公共css */
</style>
