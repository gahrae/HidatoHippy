package hidatohippy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Test;

import com.google.common.base.Predicate;

import es.usc.citius.hipster.model.function.ActionFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;

public class HidatoBoardSearcherTest {

	@Test
	public void testSearch() throws Exception {
		boolean thrownForIsolation = false;
		try {
			HidatoBoardSearcher.search(new HidatoBoard(new String[] { "XX 01 XX" }));
		} catch (IllegalArgumentException e) {
			thrownForIsolation = true;
		}

		assertTrue(
				"Expected exception when searching with board that contains isolated cells",
				thrownForIsolation);
		
		boolean thrownForDuplicateNumbers = false;
		try {
			HidatoBoardSearcher.search(new HidatoBoard(new String[] { "01 -- 03 03" }));
		} catch (IllegalArgumentException e) {
			thrownForDuplicateNumbers = true;
		}
		
		assertTrue(
				"Expected exception when searching with board that contains duplicate numbers",
				thrownForDuplicateNumbers);
		
		// TODO: Assert that given a board that the optimal path is chosen
	}

	@Test
	public void testGetActionFunction() throws Exception {
		ActionFunction<HidatoAction, HidatoBoard> af = HidatoBoardSearcher
				.getActionFunction();

		HidatoBoard noActionState = new HidatoBoard(new String[] { "01 -- XX" });
		noActionState.move(HidatoAction.MOVE_RIGHT);
		// Board should read "01 02 xx", with no moves available
		assertEquals(0,
				iteratorToFiniteStream(af.actionsFor(noActionState).iterator())
						.count());

		HidatoBoard singleActionState = new HidatoBoard(new String[] {
				"XX XX XX", "XX 01 --", "XX XX XX" });
		assertEquals(
				1,
				iteratorToFiniteStream(
						af.actionsFor(singleActionState).iterator()).count());

		HidatoBoard everyActionState = new HidatoBoard(new String[] {
				"-- -- --", "-- 01 --", "-- -- --" });
		assertEquals(
				8,
				iteratorToFiniteStream(
						af.actionsFor(everyActionState).iterator()).count());

		// Moving right will duplicate the number two. Should only generate
		// actions to "move" down.
		HidatoBoard duplicateTestActionState = new HidatoBoard(new String[] {
				"01 --", "02 XX" });
		assertEquals(
				1,
				iteratorToFiniteStream(
						af.actionsFor(duplicateTestActionState).iterator())
						.count());
	}

	static <T> Stream<T> iteratorToFiniteStream(final Iterator<T> iterator) {
		final Iterable<T> iterable = () -> iterator;
		return StreamSupport.stream(iterable.spliterator(), false);
	}

	static <T> Stream<T> iteratorToInfiniteStream(final Iterator<T> iterator) {
		return Stream.generate(iterator::next);
	}

	@Test
	public void testGetPredicateFunction() throws Exception {
		Predicate<WeightedNode<HidatoAction, HidatoBoard, Double>> pf = HidatoBoardSearcher.getPredicateFunction();
		
		HidatoBoard noActionState = new HidatoBoard(new String[] { "01 02 XX" });
		noActionState.setLocation(new Point(2,0));
		
		SearchProblem<HidatoAction, HidatoBoard, WeightedNode<HidatoAction, HidatoBoard, Double>> problem = ProblemBuilder
				.create().initialState(noActionState)
				.defineProblemWithExplicitActions()
				.useActionFunction(HidatoBoardSearcher.getActionFunction())
				.useTransitionFunction(HidatoBoardSearcher.getActionTransitionFunction())
				.useCostFunction(HidatoBoardSearcher.getCostFunction())
				.useHeuristicFunction(HidatoBoardSearcher.getHeuristicFunction())
				.build();
		
		WeightedNode<HidatoAction, HidatoBoard, Double> initialNode = problem.getInitialNode();
		
		assertTrue(pf.apply(initialNode));
		
		
	}

	@Test
	public void testGetHeuristicFunction() throws Exception {
		// TODO: Assert that moving toward goal has lower score than opposite direction
	}

	@Test
	public void testGetActionTransitionFunction() throws Exception {
		// TODO: Assert that moving in a direction changes the board so:
		// 1. getLocation() is not at direction
		// 2. value at location is +1 form previous location
	}

}
