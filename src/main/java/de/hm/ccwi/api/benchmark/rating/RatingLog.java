package de.hm.ccwi.api.benchmark.rating;

public class RatingLog {

	/**
	 * Name of API, which is rated.
	 */
	private String api;
	
	/**
	 * Accuracy (Robustheit) = (TP+TN) / (TP+TN+FP+FN).
	 */
	private double accuracy;

	/**
	 * Genauigkeit (precision P) = TP / (TP+FP).
	 */
	private double precision;

	/**
	 * Trefferquote (recall R) = TP / (TP+FN).
	 */
	private double recall;

	/**
	 * harmonischen Mittels (F1-Wert) = 2* ((P*R)/(P+R)).
	 */
	private double f1;

	/**
	 * Für jede korrekt gefundene Entität/Keyword im gesamten Goldstandard (TP).
	 */
	private double tp;

	/**
	 * Für alle falschen Entitäten/Keywords im gesamten Goldstandard (FN).
	 */
	private double fn;

	/**
	 * Für jedes andere Wort aus dem gesamten Goldstandard, das korrekterweise nicht
	 * als Entität/Keyword klassifiziert wurde (FP).
	 */
	private double fp;

	/**
	 * Für alle nicht gefundenen Entitäten/Keywords im gesamten Goldstandard (TN).
	 */
	private double tn;

	/**
	 * Constructor for new RatingLog.
	 * 
	 * @param api
	 */
	public RatingLog(String api) {
		super();
		this.api = api;
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
	public RatingLog(String api, double accuracy, double precision, double recall, double f1, double tp, double fn, double fp, double tn) {
		super();
		this.accuracy = accuracy;
		this.precision = precision;
		this.recall = recall;
		this.f1 = f1;
		this.tp = tp;
		this.fn = fn;
		this.fp = fp;
		this.tn = tn;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getF1() {
		return f1;
	}

	public void setF1(double f1) {
		this.f1 = +f1;
	}

	public double getTp() {
		return tp;
	}

	public synchronized void incrementTp(double value) {
		this.tp = tp + value;
	}

	public double getFn() {
		return fn;
	}

	public synchronized void incrementFn(double value) {
		this.fn = this.fn + value;
	}

	public double getFp() {
		return fp;
	}

	public synchronized void incrementFp(double value) {
		this.fp = this.fp + value;
	}

	public double getTn() {
		return tn;
	}

	public synchronized void incrementTn(double value) {
		this.tn = this.tn + value;
	}

	@Override
	public String toString() {
		return "RatingLog [api=" + api + ", accuracy=" + accuracy + ", precision=" + precision + ", recall=" + recall
				+ ", f1=" + f1 + ", tp=" + tp + ", fn=" + fn + ", fp=" + fp + ", tn=" + tn + "]";
	}

}
