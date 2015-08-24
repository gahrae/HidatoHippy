package hidatohippy;

import java.util.ArrayList;
import java.util.List;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.ActionFunction;
import es.usc.citius.hipster.model.function.ActionStateTransitionFunction;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.Predicate;

public class HidatoBoardSearcher {

	private HidatoBoardSearcher() {
	}


	public static Algorithm<HidatoAction, HidatoBoard, WeightedNode<HidatoAction, HidatoBoard, Double>>.SearchResult search(
			HidatoBoard initialState) {

		if (initialState.hasIsolatedNumbers()) {
			throw new IllegalArgumentException(
					"It is not possible to solve this board. Some numbers are isolated from each other.");
		}
		
		if (initialState.hasDuplicateNumbers()) {
			throw new IllegalArgumentException(
					"The board has duplicate numbers. Please check your input.");
		}

		SearchProblem<HidatoAction, HidatoBoard, WeightedNode<HidatoAction, HidatoBoard, Double>> problem = ProblemBuilder
				.create().initialState(initialState)
				.defineProblemWithExplicitActions()
				.useActionFunction(getActionFunction())
				.useTransitionFunction(getActionTransitionFunction())
				.useCostFunction(getCostFunction())
				.useHeuristicFunction(getHeuristicFunction())
				.build();

		Predicate<WeightedNode<HidatoAction, HidatoBoard, Double>> predicate = getPredicateFunction();

		return Hipster.createAStar(problem).search(predicate);
	}

	public static ActionFunction<HidatoAction, HidatoBoard> getActionFunction() {
		ActionFunction<HidatoAction, HidatoBoard> af = new ActionFunction<HidatoAction, HidatoBoard>() {
			public Iterable<HidatoAction> actionsFor(HidatoBoard state) {
				List<HidatoAction> directions = new ArrayList<HidatoAction>();
				for (HidatoAction direction : HidatoAction.values()) {
					if (state.canMove(direction)) {
						directions.add(direction);
					}
				}
				return directions;				
			}
		};
		return af;
	}

	public static ActionStateTransitionFunction<HidatoAction, HidatoBoard> getActionTransitionFunction() {
		ActionStateTransitionFunction<HidatoAction, HidatoBoard> atf = new ActionStateTransitionFunction<HidatoAction, HidatoBoard>() {
			public HidatoBoard apply(HidatoAction action, HidatoBoard state) {
				HidatoBoard next = new HidatoBoard(state);
				try {
					next.move(action);
				} catch (IllegalMoveException e) {
					throw new RuntimeException(e);
				}
				return next;
			}
		};
		return atf;
	}

	public static CostFunction<HidatoAction, HidatoBoard, Double> getCostFunction() {
		CostFunction<HidatoAction, HidatoBoard, Double> cf = new CostFunction<HidatoAction, HidatoBoard, Double>() {
			public Double evaluate(Transition<HidatoAction, HidatoBoard> transition) {
				return 1d;
			}
		};
		return cf;
	}


	public static HeuristicFunction<HidatoBoard, Double> getHeuristicFunction() {
		HeuristicFunction<HidatoBoard, Double> hf = new HeuristicFunction<HidatoBoard, Double>() {
			public Double estimate(HidatoBoard state) {
				Integer next = state.getNextNumber();
				if (next != null) {
					Integer distance = state.getDistanceTo(next);
					if (! (distance == null)) {
						return distance.doubleValue();
					}
				}

				return (double) state.getWidth() * state.getHeight();
			}
		};

		return hf;
	}


	public static Predicate<WeightedNode<HidatoAction, HidatoBoard, Double>> getPredicateFunction() {
		Predicate<WeightedNode<HidatoAction, HidatoBoard, Double>> predicate = new Predicate<WeightedNode<HidatoAction, HidatoBoard, Double>>() {
			public boolean apply(WeightedNode<HidatoAction, HidatoBoard, Double> input) {
				boolean solutionFound = input.state().connectedToLastNumber();
				// Debugging...
//				System.out.println("Predicate function");
//				System.out.println(input.state().boardAsString());
//				System.out.println("Targeting: " + input.state().getNextNumber());
//				System.out.println("Cost: " + input.getCost());
//				System.out.println("Heuristic: " + input.getEstimation());
//				System.out.println("Score: " + input.getScore());
//				System.out.println();
				
				return solutionFound;
			}
		};
		return predicate;
	}
}
