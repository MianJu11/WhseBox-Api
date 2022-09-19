var vm = new Vue({
	el: '#app',
	data: {
		page: 1,
		webkey:"",
		isLogin:0,
		alert:0,
		alertText:"",
		isMenu:false,

		//基本信息
		webinfoTitle:"",
		webinfoUrl:"",

		webinfoUsertime:"",
		webinfoUploadUrl:"",
		webinfoAvatar:"",
		pexelsKey:"",
		scale:"",

		clock:"",
		vipPrice:"",
		vipDay:"",
		vipDiscount:"",
		isEmail:"",
		isInvite:"",
        adAwardPoint:"",
        adAwardPerDay:"",
		adRwPerDay:"",
		mianappa:"",
        mianappb:"",
        mianappc:"",

		versionCode:"",
		versionUrl:"",
		version:"",
		versionIntro:"",

		adsxili:"",
        mianadszl:"",
        miangg:"",
        miandhlname:"",
        miandhlurl:"",
        mianqq:"",
        miankime:"",

        // app检测配置
        bm0shifou0action: '',
        bm0shifou0pname: '',
        bm0jiance0action: '',
        bm0jiance0pname: '',
        bm0xiazai: '',
        bm0neirong: '',
        whseApp: '',
        whseApi: '',
        whseApphttp: '',

        //全局弹窗
        gg0cishu: '',
        gg0neirong: '',
        gg0lianjie: '',

		//系统密钥
		webinfoKey:"",

		//邮箱配置
		mailHost:'',
		mailUsername:'',
		mailPassword:'',
		//mysql配置
		dataUrl:"",
		dataUsername:"",
		dataPassword:"",
		dataPrefix:"",
		//Redis配置
		redisHost:'',
		redisPassword:'',
		redisPort:'',
		redisPrefix:'',
		//缓存配置
		usertime:'',
		contentCache:'',
		contentInfoCache:'',
		CommentCache:'',
		userCache:'',
		//COS
		cosAccessKey:'',
		cosSecretKey:'',
		cosBucket:'',
		cosBucketName:'',
		cosPath:'',
		cosPrefix:'',
		//OSS
		aliyunEndpoint:'',
		aliyunAccessKeyId:'',
		aliyunAccessKeySecret:'',
		aliyunAucketName:'',
		aliyunUrlPrefix:'',
		aliyunFilePrefix:'',
		//FTP
		ftpHost:'',
		ftpPort:'',
		ftpUsername:'',
		ftpPassword:'',
		ftpBasePath:'',
		//支付宝
		alipayAppId:'',
		alipayPrivateKey:'',
		alipayPublicKey:'',
		alipayNotifyUrl:'',
		//微信支付
		appletsAppid:'',
		appletsSecret:'',
		qqAppletsAppid:'',
		qqAppletsSecret:'',
		wxpayAppId:'',
		wxpayMchId:'',
		wxpayKey:'',
		wxpayNotifyUrl:'',
		//配置信息
		applicationText:"",
		//评论配置
		auditlevel:"",
		forbidden:"",
		//易支付
		epayUrl:"",
		epayPid:"",
		epayKey:"",
		epayNotifyUrl:""
	},
	created(){
		var that = this;
		if(localStorage.getItem('WhseApipage')){
			that.page = Number(localStorage.getItem('WhseApipage'));
		}
		if(localStorage.getItem('webkey')){
			that.webkey = localStorage.getItem('webkey');
			that.isLogin = 1;
		}else{
			that.isLogin = 0;
		}
		that.getAll();
		that.getConfigAll();
		that.getApplicationText();
	},
	mounted(){
		var that = this;
	},
	methods: {
		toMenu(){
			var that = this;
			that.isMenu = !that.isMenu;
		},
		toAlert(text){
			var that = this;
			if(that.alert==1){
				return false;
			}
			that.alert=1;
			that.alertText = text;
			var timer = setTimeout(function() {
				that.alert=0;
				that.alertText = "";
			}, 2000)
		},
		toPage(i){
			var that = this;
			that.page = i;
			localStorage.setItem('WhseApipage',i);
		},
		// API重启
		restart(){
			var that = this;
			var url = "/system/restart"
			axios.get(url,{
				params:{
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
			}).catch(function (error) {
				console.log(error);
			});
		},
		outSystem(){
			var that = this;
			localStorage.removeItem('webkey');
			localStorage.removeItem('WhseApipage');
			that.isLogin = 0;
			that.webkey = "";
		},
		toSystem(){
			var that = this;
			var url = "/system/isKey"
			axios.get(url,{
				params:{
					"webkey":that.webkey,
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					localStorage.setItem('webkey',that.webkey);
					that.isLogin=1;
					that.getAll();
					that.getApplicationText();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		//移除数据中的空对象
		removeObjectEmptyKey(json) {
			var value;
			for (var key in json) {
				if (json.hasOwnProperty(key)) {
					value = json[key];
					if (value === undefined || value === '' || value === null) {
						delete json[key]
					}
				}
			}
			return json;
		},
		getConfigAll(){
			var that = this;
			var url = "/system/getApiConfig"
			axios.get(url,{
				params:{
					"webkey":that.webkey,
				}
			}).then(function(res){
				if(res.data.code==1){
					that.webinfoTitle=res.data.data.webinfoTitle;
					that.webinfoUrl=res.data.data.webinfoUrl;
					that.webinfoUsertime=res.data.data.webinfoUsertime;
					that.webinfoUploadUrl=res.data.data.webinfoUploadUrl;
					that.webinfoAvatar=res.data.data.webinfoAvatar;
					that.pexelsKey = res.data.data.pexelsKey;


					//COS
					that.cosAccessKey=res.data.data.cosAccessKey;
					that.cosSecretKey=res.data.data.cosSecretKey;
					that.cosBucket=res.data.data.cosBucket;
					that.cosBucketName=res.data.data.cosBucketName;
					that.cosPath=res.data.data.cosPath;
					that.cosPrefix=res.data.data.cosPrefix;
					//Oss
					that.aliyunEndpoint=res.data.data.aliyunEndpoint;
					that.aliyunAccessKeyId=res.data.data.aliyunAccessKeyId;
					that.aliyunAccessKeySecret=res.data.data.aliyunAccessKeySecret;
					that.aliyunAucketName=res.data.data.aliyunAucketName;
					that.aliyunUrlPrefix=res.data.data.aliyunUrlPrefix;
					that.aliyunFilePrefix=res.data.data.aliyunFilePrefix;
					//ftp
					that.ftpHost=res.data.data.ftpHost;
					that.ftpPort=res.data.data.ftpPort;
					that.ftpUsername=res.data.data.ftpUsername;
					that.ftpPassword=res.data.data.ftpPassword;
					that.ftpBasePath=res.data.data.ftpBasePath;
					//支付宝
					that.alipayAppId=res.data.data.alipayAppId;
					that.alipayPrivateKey=res.data.data.alipayPrivateKey;
					that.alipayPublicKey=res.data.data.alipayPublicKey;
					that.alipayNotifyUrl=res.data.data.alipayNotifyUrl;

					//微信支付
					that.appletsAppid=res.data.data.appletsAppid;
					that.appletsSecret=res.data.data.appletsSecret;
					that.qqAppletsAppid=res.data.data.qqAppletsAppid;
					that.qqAppletsSecret=res.data.data.qqAppletsSecret;
					that.wxpayAppId=res.data.data.wxpayAppId;
					that.wxpayMchId=res.data.data.wxpayMchId;
					that.wxpayKey=res.data.data.wxpayKey;
					that.wxpayNotifyUrl=res.data.data.wxpayNotifyUrl;

					//支付模块
					that.scale = res.data.data.scale;
					that.clock=res.data.data.clock;
					that.vipPrice=res.data.data.vipPrice;
					that.vipDay=res.data.data.vipDay;
					that.vipDiscount=res.data.data.vipDiscount;
					that.isEmail=res.data.data.isEmail;
					that.isInvite=res.data.data.isInvite;
                    that.adAwardPoint = res.data.data.adAwardPoint;
                    that.adAwardPerDay = res.data.data.adAwardPerDay;
					that.adRwPerDay = res.data.data.adRwPerDay;

					that.adsxili=res.data.data.adsxili;
					that.mianadszl=res.data.data.mianadszl;
					that.miangg=res.data.data.miangg;
					that.miandhlname=res.data.data.miandhlname;
					that.miandhlurl=res.data.data.miandhlurl;
					that.mianqq=res.data.data.mianqq;
					that.miankime=res.data.data.miankime;
					that.mianappa=res.data.data.mianappa;
					that.mianappb=res.data.data.mianappb;
					that.mianappc=res.data.data.mianappc;

					that.versionCode=res.data.data.versionCode;
					that.versionUrl=res.data.data.versionUrl;
					that.version=res.data.data.version;
					that.versionIntro=res.data.data.versionIntro;

					//易支付

					that.epayUrl=res.data.data.epayUrl;
					that.epayPid=res.data.data.epayPid;
					that.epayKey=res.data.data.epayKey;
					that.epayNotifyUrl=res.data.data.epayNotifyUrl;

					// app检测配置赋值
                    that.bm0shifou0action = res.data.data.bm0shifou0action;
                    that.bm0shifou0pname = res.data.data.bm0shifou0pname;
                    that.bm0jiance0action = res.data.data.bm0jiance0action;
                    that.bm0jiance0pname = res.data.data.bm0jiance0pname;
                    that.bm0xiazai = res.data.data.bm0xiazai;
                    that.bm0neirong = res.data.data.bm0neirong;
					that.whseApp = res.data.data.whseApp;
					that.whseApi = res.data.data.whseApi;
					that.whseApphttp = res.data.data.whseApphttp;

				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		getAll(){
			var that = this;
			that.getConfigAll();
			var url = "/system/allConfig"
			axios.get(url,{
			        params:{
						"webkey":that.webkey,
			        }
			}).then(function(res){
				if(res.data.code==1){

					that.webinfoKey=res.data.data.webinfoKey;


					//邮箱信息
					that.mailHost=res.data.data.mailHost;
					that.mailUsername=res.data.data.mailUsername;
					that.mailPassword=res.data.data.mailPassword;
					//mysql配置
					that.dataUrl=res.data.data.dataUrl;
					that.dataUsername=res.data.data.dataUsername;
					that.dataPassword=res.data.data.dataPassword;
					that.dataPrefix=res.data.data.dataPrefix;
					//Redis配置
					that.redisHost=res.data.data.redisHost;
					that.redisPassword=res.data.data.redisPassword;
					that.redisPort=res.data.data.redisPort;
					that.redisPrefix=res.data.data.redisPrefix;
					//缓存配置
					that.usertime=res.data.data.usertime;
					that.contentCache=res.data.data.contentCache;
					that.contentInfoCache=res.data.data.contentCache;
					that.CommentCache=res.data.data.CommentCache;
					that.userCache=res.data.data.userCache;
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		getApplicationText(){
			var that = this;
			var url = "/system/getConfig"
			axios.get(url,{
				params:{
					"webkey":that.webkey,
				}
			}).then(function(res){
				if(res.data.code==1){
					that.applicationText = unescape(res.data.data);
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveCache(){
			var that = this;
			var url = "/system/setupCache"
			var data={
				usertime:that.usertime,
				contentCache:that.contentCache,
				contentInfoCache:that.contentInfoCache,
				CommentCache:that.CommentCache,
				userCache:that.userCache,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveWebKey(){
			var that = this;
			var url = "/system/setupWebKey"
			var data={
				webinfoKey:that.webinfoKey,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveWebinfo(){
			var that = this;
			var url = "/system/apiConfigUpdate"
			var data={
				webinfoTitle:that.webinfoTitle,
				webinfoUrl:that.webinfoUrl,
				webinfoUsertime:that.webinfoUsertime,
				webinfoUploadUrl:that.webinfoUploadUrl,
				webinfoAvatar:that.webinfoAvatar,
				pexelsKey:that.pexelsKey,
				isEmail:that.isEmail,
				isInvite:that.isInvite,
				auditlevel:that.auditlevel,
				forbidden:that.forbidden,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveAssets(){
			var that = this;
			var url = "/system/apiConfigUpdate"
			var data={
				scale:that.scale,
				clock:that.clock,
				vipPrice:that.vipPrice,
				vipDay:that.vipDay,
				vipDiscount:that.vipDiscount,
				adsxili: that.adsxili,
				mianadszl: that.mianadszl,
				adAwardPoint: that.adAwardPoint,
				adAwardPerDay: that.adAwardPerDay,
				adRwPerDay: that.adRwPerDay,

				miangg: that.miangg,
				miandhlname: that.miandhlname,
				miandhlurl: that.miandhlurl,
				mianqq: that.mianqq,
				miankime: that.miankime,
				mianappa: that.mianappa,
				mianappb: that.mianappb,
				mianappc: that.mianappc,
				versionCode: that.versionCode,
				versionUrl: that.versionUrl,
				version: that.version,
				versionIntro: that.versionIntro,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveEmail(){
			var that = this;
			var url = "/system/setupEmail"
			var data={
				mailHost:that.mailHost,
				mailUsername:that.mailUsername,
				mailPassword:that.mailPassword,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveMysql(){
			var that = this;
			var url = "/system/setupMysql"
			var data={
				dataUrl:that.dataUrl,
				dataUsername:that.dataUsername,
				dataPassword:that.dataPassword,
				dataPrefix:that.dataPrefix
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveRedis(){
			var that = this;
			var url = "/system/setupRedis"
			var data={
				redisHost:that.redisHost,
				redisPassword:that.redisPassword,
				redisPort:that.redisPort,
				redisPrefix:that.pexelsKey,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveCos(){
			var that = this;
			var url = "/system/apiConfigUpdate"
			var data={
				cosAccessKey:that.cosAccessKey,
				cosSecretKey:that.cosSecretKey,
				cosBucket:that.cosBucket,
				cosBucketName:that.cosBucketName,
				cosPath:that.cosPath,
				cosPrefix:that.cosPrefix,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveOss(){
			var that = this;
			var url = "/system/apiConfigUpdate"
			var data={
				aliyunEndpoint:that.aliyunEndpoint,
				aliyunAccessKeyId:that.aliyunAccessKeyId,
				aliyunAccessKeySecret:that.aliyunAccessKeySecret,
				aliyunAucketName:that.aliyunAucketName,
				aliyunUrlPrefix:that.aliyunUrlPrefix,
				aliyunFilePrefix:that.aliyunFilePrefix,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},

		saveFTP(){
			var that = this;
			var url = "/system/apiConfigUpdate"
			var data={
				ftpHost:that.ftpHost,
				ftpPort:that.ftpPort,
				ftpUsername:that.ftpUsername,
				ftpPassword:that.ftpPassword,
				ftpBasePath:that.ftpBasePath,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveAlipay(){
			var that = this;
			var url = "/system/apiConfigUpdate"
			var data={
				alipayAppId:that.alipayAppId,
				alipayPrivateKey:that.alipayPrivateKey,
				alipayPublicKey:that.alipayPublicKey,
				alipayNotifyUrl:that.alipayNotifyUrl,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},
		saveWxpay(){
			var that = this;
			var url = "/system/apiConfigUpdate"
			var data={
				wxpayAppId:that.wxpayAppId,
				wxpayMchId:that.wxpayMchId,
				wxpayKey:that.wxpayKey,
				wxpayNotifyUrl:that.wxpayNotifyUrl,
				appletsAppid:that.appletsAppid,
				appletsSecret:that.appletsSecret,
				qqAppletsAppid:that.qqAppletsAppid,
				qqAppletsSecret:that.qqAppletsSecret,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},

		saveEpay(){
			var that = this;
			var url = "/system/apiConfigUpdate"
			var data={
				epayUrl:that.epayUrl,
				epayPid:that.epayPid,
				epayKey:that.epayKey,
				epayNotifyUrl:that.epayNotifyUrl,
			}
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":JSON.stringify(that.removeObjectEmptyKey(data))
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},

		saveConfig(){
			var that = this;
			var url = "/system/setupConfig"
			axios.get(url,{
				params:{
					"webkey":that.webkey,
					"params":that.applicationText
				}
			}).then(function(res){
				that.toAlert(res.data.msg);
				if(res.data.code==1){
					that.getAll();
					that.getApplicationText();
				}else{
					that.outSystem();
				}
			}).catch(function (error) {
				console.log(error);
			});
		},

		// 对手app检测参数
		saveAppcheck(){
            var that = this;
            var url = "/system/apiConfigUpdate"
            var data={
                bm0shifou0action: that.bm0shifou0action,
                bm0shifou0pname: that.bm0shifou0pname,
                bm0jiance0action: that.bm0jiance0action,
                bm0jiance0pname: that.bm0jiance0pname,
                bm0xiazai: that.bm0xiazai,
                bm0neirong: that.bm0neirong,

				whseApp: that.whseApp,
				whseApi: that.whseApi,
				whseApphttp: that.whseApphttp,

                gg0cishu: that.gg0cishu,
                gg0neirong: that.gg0neirong,
                gg0lianjie: that.gg0lianjie,
            }
            axios.get(url,{
                params:{
                    "webkey":that.webkey,
                    "params":JSON.stringify(that.removeObjectEmptyKey(data))
                }
            }).then(function(res){
                that.toAlert(res.data.msg);
                if(res.data.code==1){
                    that.getAll();
                }else{
                    that.outSystem();
                }
            }).catch(function (error) {
                console.log(error);
            });
        },
	}
})
