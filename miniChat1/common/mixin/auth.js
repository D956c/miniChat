// 引入工具库
export default {
  onShow() {
    let token = uni.getStorageSync('token');
    if (!token) {
      return uni.reLaunch({
        url: "/pages/common/login/login",
      });
    }
  },
  methods:{
  		navigate(path){
  			uni.navigateTo({
  				url: '/pages/'+path,
  			});
  		},
  		// 返回并提示
  		backToast(msg = '非法参数'){
  			uni.showToast({
  				title: msg,
  				icon:"none"
  			});
  			uni.navigateBack({
  				delta: 1,
  			});
  		}
  	}
};