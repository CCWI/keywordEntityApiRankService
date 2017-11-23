package de.hm.ccwi.api.benchmark.rating.dto;

public class RatingLog {

	/**
	 * Name of API, which is rated.
	 */
	private String api;

	/**
	 * Accuracy (Robustheit) = (TP+TN) / (TP+TN+FP+FN).
	 */
	private Double accuracy;

	/**
	 * Genauigkeit (precision P) = TP / (TP+FP).
	 */
	private Double precision;

	/**
	 * Trefferquote (recall R) = TP / (TP+FN).
	 */
	private Double recall;

	/**
	 * harmonischen Mittels (F1-Wert) = 2* ((P*R)/(P+R)).
	 */
	private Double f1;

	/**
	 * Für jede korrekt gefundene Entität/Keyword im gesamten Goldstandard (TP).
	 */
	private int tp;

	/**
	 * Für alle falschen Entitäten/Keywords im gesamten Goldstandard (FN).
	 */
	private int fn;

	/**
	 * Für jedes andere Wort aus dem gesamten Goldstandard, das korrekterweise nicht
	 * als Entität/Keyword klassifiziert wurde (FP).
	 */
	private int fp;

	/**
	 * Für alle nicht gefundenen Entitäten/Keywords im gesamten Goldstandard (TN).
	 */
	private int tn;

	/**
	 * Analyzed Object.
	 */
	private EntityKeywordLog entityKeywordLog;

	/**
	 * Constructor for new RatingLog.
	 * 
	 * @param api
	 */
	public RatingLog(String api, EntityKeywordLog entityKeywordLog) {
		super();
		this.api = api;
		this.entityKeywordLog = entityKeywordLog;
	}

	/**
	 * Constructor for all fields.
	 * 
	 * @param api
	 * @param accuracy
	 * @param precision
	 * @param recall
	 * @param f1
	 * @param tP
	 * @param fN
	 * @param fP
	 * @param tN
	 */
	public RatingLog(String api, Double accuracy, Double precision, Double recall, Double f1, int tp, int fn,
			int fp, int tn, EntityKeywordLog entityKeywordLog) {
		super();
		this.accuracy = accuracy;
		this.precision = precision;
		this.recall = recall;
		this.f1 = f1;
		this.tp = tp;
		this.fn = fn;
		this.fp = fp;
		this.tn = tn;
		this.entityKeywordLog = entityKeywordLog;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public Double getPrecision() {
		return precision;
	}

	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	public Double getRecall() {
		return recall;
	}

	public void setRecall(Double recall) {
		this.recall = recall;
	}

	public Double getF1() {
		return f1;
	}

	public void setF1(Double f1) {
		this.f1 = +f1;
	}

	public int getTp() {
		return tp;
	}

	public synchronized void incrementTp(int value) {
		this.tp = tp + value;
	}

	public int getFn() {
		return fn;
	}

	public synchronized void incrementFn(int value) {
		this.fn = this.fn + value;
	}

	public int getFp() {
		return fp;
	}

	public synchronized void incrementFp(int value) {
		this.fp = this.fp + value;
	}

	public int getTn() {
		return tn;
	}

	public synchronized void incrementTn(int value) {
		this.tn = this.tn + value;
	}
	
	public EntityKeywordLog getEntityKeywordLog() {
		return entityKeywordLog;
	}

	public void setEntityKeywordLog(EntityKeywordLog entityKeywordLog) {
		this.entityKeywordLog = entityKeywordLog;
	}

	@Override
	public String toString() {
		return "RatingLog [api=" + api + ", accuracy=" + accuracy + ", precision=" + precision + ", recall=" + recall
				+ ", f1=" + f1 + ", tp=" + tp + ", fn=" + fn + ", fp=" + fp + ", tn=" + tn + ", entityKeywordLog="
				+ entityKeywordLog + "]";
	}

}
