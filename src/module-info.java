/**
 * 
 */
/**
 * 
 */
module module {
	requires java.sql;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;

	opens main to javafx.graphics;
	opens models to javafx.base;
}