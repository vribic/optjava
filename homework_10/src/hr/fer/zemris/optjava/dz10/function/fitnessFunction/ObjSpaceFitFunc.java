package hr.fer.zemris.optjava.dz10.function.fitnessFunction;

import java.util.LinkedList;

import hr.fer.zemris.optjava.dz10.algorithm.NSGA2Solution;
import hr.fer.zemris.optjava.dz10.interfaces.MOOPProblem;

/**
 * ObjSpaceFitFunc implements the solution distance in the objective space
 * domain.
 * 
 * @author Viran
 *
 */
public class ObjSpaceFitFunc extends FitnessFunction {

	/**
	 * FitnessFunction constructor.
	 * 
	 * @param moopProblem
	 *            Problem we are trying to solve.
	 */
	public ObjSpaceFitFunc(MOOPProblem moopProblem) {
		super(moopProblem);
	}

	@Override
	protected void calculateDistance(LinkedList<NSGA2Solution> population) {
		subjectRelations = new double[population.size()][population.size()];

		double[] minValVect = new double[moopProblem.getObjectiveSpaceDim()];
		double[] maxValVect = new double[moopProblem.getObjectiveSpaceDim()];

		for (int popSubjIndex = 0; popSubjIndex < population.size(); popSubjIndex++) {
			NSGA2Solution solution = population.get(popSubjIndex);
			double[] vector = new double[getObjectiveSpaceDim()];

			moopProblem.evaluateSolution(solution.getValuesVector(), vector);

			if (popSubjIndex == 0) {
				for (int vecIndex = 0; vecIndex < vector.length; vecIndex++) {
					minValVect[vecIndex] = vector[vecIndex];
					maxValVect[vecIndex] = vector[vecIndex];
				}
			} else {
				for (int vecIndex = 0; vecIndex < vector.length; vecIndex++) {
					if (minValVect[vecIndex] > vector[vecIndex]) {
						minValVect[vecIndex] = vector[vecIndex];
					}
					if (maxValVect[vecIndex] < vector[vecIndex]) {
						maxValVect[vecIndex] = vector[vecIndex];
					}
				}
			}
		}

		for (int firstSubjectIndex = 0; firstSubjectIndex < population.size(); firstSubjectIndex++) {
			for (int secondSubjectIndex = firstSubjectIndex + 1; secondSubjectIndex < population
					.size(); secondSubjectIndex++) {

				NSGA2Solution firstSolution = population.get(firstSubjectIndex);
				NSGA2Solution secondSolution = population.get(secondSubjectIndex);

				double[] firstValues = new double[getObjectiveSpaceDim()];
				double[] secondValues = new double[getObjectiveSpaceDim()];

				moopProblem.evaluateSolution(firstSolution.getValuesVector(), firstValues);
				moopProblem.evaluateSolution(secondSolution.getValuesVector(), secondValues);

				double d = 0;

				for (int dim = 0; dim < firstValues.length; dim++) {
					double tmp = (firstValues[dim] - secondValues[dim]) / (maxValVect[dim] - minValVect[dim]);
					tmp = Math.pow(tmp, 2);
					d += tmp;
				}
				subjectRelations[firstSubjectIndex][secondSubjectIndex] = subjectRelations[secondSubjectIndex][firstSubjectIndex] = Math
						.sqrt(d);

			}
		}

	}

}
