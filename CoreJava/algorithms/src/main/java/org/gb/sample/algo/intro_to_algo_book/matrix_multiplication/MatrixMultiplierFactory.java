package org.gb.sample.algo.intro_to_algo_book.matrix_multiplication;

final class MatrixMultiplierFactory {

	private MatrixMultiplierFactory() {

	}

	static MatrixMultiplier getSerialSquareMatrixMultiplier() {
		return new SerialSquareMatrixMultiplier();
	}

	static MatrixMultiplier getSerialRecursiveSquareMatrixMultiplier() {
		return new SerialRecursiveSquareMatrixMultiplier();
	}
	
	static MatrixMultiplier getSerialRecursiveArrayCopySquareMatrixMultiplier() {
		return new SerialRecursiveArrayCopySquareMatrixMultiplier();
	}
}
