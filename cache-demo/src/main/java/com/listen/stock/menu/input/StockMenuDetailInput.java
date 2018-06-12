package com.listen.stock.menu.input;



public class StockMenuDetailInput {
    private String symbol;//*代码*/
    private String name;
    private String trade;// 11.590,/*最新价*/
    private String pricechange; //-0.130,/*涨跌额*/
    private String changepercent;// -1.109,/*涨跌幅*/
    private String buy; // 11.590,/*买入*/
    private String sell; //11.600,/*卖出*/
    private String settlement; //11.720,/*昨收*/
    private String open;// 11.670,/*今开*/
    private String high;// 11.800,/*最高*/
    private String low; //11.570,/*最低*/
    private String volume;// 38781,/*成交量*/
    private String amount;// 45385925,/*成效额*/
    private String code;//600004,/*简码*/
    private String ticktime;// 15:00:00/*时间*/
	
    public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTrade() {
		return trade;
	}
	public void setTrade(String trade) {
		this.trade = trade;
	}
	public String getPricechange() {
		return pricechange;
	}
	public void setPricechange(String pricechange) {
		this.pricechange = pricechange;
	}
	public String getChangepercent() {
		return changepercent;
	}
	public void setChangepercent(String changepercent) {
		this.changepercent = changepercent;
	}
	public String getBuy() {
		return buy;
	}
	public void setBuy(String buy) {
		this.buy = buy;
	}
	public String getSell() {
		return sell;
	}
	public void setSell(String sell) {
		this.sell = sell;
	}
	public String getSettlement() {
		return settlement;
	}
	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}
	public String getOpen() {
		return open;
	}
	public void setOpen(String open) {
		this.open = open;
	}
	public String getHigh() {
		return high;
	}
	public void setHigh(String high) {
		this.high = high;
	}
	public String getLow() {
		return low;
	}
	public void setLow(String low) {
		this.low = low;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTicktime() {
		return ticktime;
	}
	public void setTicktime(String ticktime) {
		this.ticktime = ticktime;
	}
}
