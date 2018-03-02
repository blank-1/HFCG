package com.xt.cfp.core.games;

import java.math.BigDecimal;
import java.util.Random;

import com.xt.cfp.core.enums.EnumsCanDescribed;

public class LotteryDraw {
	
	public static int[] LEVEL_ONE=new int[]{49,79,89,99,100};
	public static int[] LEVEL_TWO=new int[]{39,69,89,99,100};
	public static int[] LEVEL_THREE=new int[]{39,69,89,94,99};
	public static int[] LEVEL_FOUR=new int[]{29,49,69,89,99};
	public static int[] LEVEL_FIVE=new int[]{19,29,59,89,99};
	public static int[] LEVEL_SIX=new int[]{9,29,59,89,99};
	public static int[] LEVEL_SEVEN=new int[]{9,29,69,99,100};
	public static int[] LEVEL_EIGHT=new int[]{9,19,59,99,100};
	
	public enum LotteryResult implements EnumsCanDescribed{
		TYPE_ONE("1","感谢参与"),
		TYPE_TWO("2","三个月加息0.5%"),
		TYPE_THREE("3","三个月加息0.8%"),
		TYPE_FOUR("4","三个月加息1%"),
		TYPE_FIVE("5","三个月加息1.2%"),
	    ;
		
		private final String value;
	    private final String desc;
	    
	    LotteryResult(String value, String desc){
	    	this.value = value;
	        this.desc = desc;
	    }
	    
	    @Override
	    public String getDesc() {
	        return desc;
	    }

	    @Override
	    public String getValue() {
	        return value;
	    }
	}
	
	public enum Lottery implements EnumsCanDescribed{
		TYPE_ONE("21","感谢参与"),
		TYPE_TWO("22","0.5%25"),
		TYPE_THREE("23","0.8%25"),
		TYPE_FOUR("24","1%25"),
		TYPE_FIVE("25","1.2%25"),
	    ;
		
		private final String value;
	    private final String desc;
	    
	    Lottery(String value, String desc){
	    	this.value = value;
	        this.desc = desc;
	    }
	    
	    @Override
	    public String getDesc() {
	        return desc;
	    }

	    @Override
	    public String getValue() {
	        return value;
	    }
	}
	
	public LotteryDraw(int[] nums) {
		Random r=new Random();
		int num=r.nextInt(100);
		for (int i = 0; i < nums.length; i++) {
			if(num<=nums[i]){
				switch (i) {
					case 0:
						result=LotteryResult.TYPE_ONE;
						lotteryValue=Lottery.TYPE_ONE;
						break;
					case 1:
						result=LotteryResult.TYPE_TWO;
						lotteryValue=Lottery.TYPE_TWO;
						break;
					case 2:
						result=LotteryResult.TYPE_THREE;
						lotteryValue=Lottery.TYPE_THREE;
						break;
					case 3:
						result=LotteryResult.TYPE_FOUR;
						lotteryValue=Lottery.TYPE_FOUR;
						break;
					case 4:
						result=LotteryResult.TYPE_FIVE;
						lotteryValue=Lottery.TYPE_FIVE;
						break;
					default:
						result=LotteryResult.TYPE_ONE;
						lotteryValue=Lottery.TYPE_ONE;
						break;
				}
				break;
			}
		}
	}
	
	private LotteryResult result;
	private Lottery lotteryValue;
	
	public LotteryResult getLotteryResult(){
		return result;
	}
	
	public Lottery getLotteryValue(){
		return lotteryValue;
	}
	
	/**
	 * 奖品已抽完，则所有抽中的奖品变为TYPE_TWO
	 */
	public void changeLottertType(){
		result=LotteryResult.TYPE_TWO;
		lotteryValue=Lottery.TYPE_TWO;
	}
	
	public static int[] calLevel(BigDecimal num){
		int[] nums=null;
		if(num.compareTo(new BigDecimal(5000))<0){
			nums=LEVEL_ONE;
		}else if(num.compareTo(new BigDecimal(10000))<0){
			nums=LEVEL_TWO;
		}else if(num.compareTo(new BigDecimal(50000))<0){
			nums=LEVEL_THREE;
		}else if(num.compareTo(new BigDecimal(100000))<0){
			nums=LEVEL_FOUR;
		}else if(num.compareTo(new BigDecimal(200000))<0){
			nums=LEVEL_FIVE;
		}else if(num.compareTo(new BigDecimal(500000))<0){
			nums=LEVEL_SIX;
		}else if(num.compareTo(new BigDecimal(1000000))<0){
			nums=LEVEL_SEVEN;
		}else if(num.compareTo(new BigDecimal(1000000000))<0){
			nums=LEVEL_EIGHT;
		}else{
			nums=LEVEL_ONE;
		}
		return nums;
	}
	
	public static void main(String[] args) {
		int[] nums=new int[]{0,0,0,0,0};
		for (int i = 0; i < 100; i++) {
			LotteryDraw d=new LotteryDraw(LEVEL_FOUR);
			switch (d.getLotteryResult().getValue()) {
				case "1":
					nums[0]++;
				break;
				case "2":
					nums[1]++;
					break;
				case "3":
					nums[2]++;
					break;
				case "4":
					nums[3]++;
					break;
				case "5":
					nums[4]++;
					break;
			}
		}
		for (int i : nums) {
			System.out.println(i);
		}
	}
}
