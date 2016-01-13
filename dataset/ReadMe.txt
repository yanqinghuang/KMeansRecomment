1) 16维数用户行为特征:
	F=(clickadv, record, weiboxiu_browser, weiboxiu_share, weiboxiu_url, feilaixun_browser, 
	   feilaixun_share, caijingyuan_browser, caijingyuan_share, zhiyinbang_browser, zhiyinbang_share, 
	   zhiyinbang_phone, huobaodian_browser, huobaodian_share, huobaodian_phone, qiangpianyi_browser)

2) 每行18维特数据格式：
uid	reg?	
104	0 	102	0:3	1:2	…	15:7其中：
第二项表示是否为注册用户, 0 - 未注册  1 - 注册
第三项表示相对起始时间天数，起始时间2012/12/1
i:a表示F(i)=a 如: 0:3表示F(0)=3 即clickadv = 3