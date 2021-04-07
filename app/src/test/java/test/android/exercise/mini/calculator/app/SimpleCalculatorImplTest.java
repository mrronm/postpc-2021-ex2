package test.android.exercise.mini.calculator.app;

import android.exercise.mini.calculator.app.SimpleCalculatorImpl;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

public class SimpleCalculatorImplTest
{

    @Test
    public void when_noInputGiven_then_outputShouldBe0()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        assertEquals("0", calculatorUnderTest.output());
    }

    @Test
    public void when_inputIsPlus_then_outputShouldBe0Plus()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertPlus();
        assertEquals("0+", calculatorUnderTest.output());
    }


    @Test
    public void when_inputIsMinus_then_outputShouldBeCorrect()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertMinus();
        String expected = "0-";
        assertEquals(expected, calculatorUnderTest.output());
    }

    @Test
    public void when_callingInsertDigitWithIllegalNumber_then_exceptionShouldBeThrown()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        try
        {
            calculatorUnderTest.insertDigit(357);
            fail("should throw an exception and not reach this line");
        } catch (RuntimeException e)
        {
            // good :)
        }
    }


    @Test
    public void when_callingDeleteLast_then_lastOutputShouldBeDeleted()
    {
        // todo: implement test
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        // delete 0 remains 0
        calculatorUnderTest.deleteLast();
        assertEquals("0", calculatorUnderTest.output());
        // delete on "0+" should show 0
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.deleteLast();
        assertEquals("0", calculatorUnderTest.output());
        // insert 1 digit and delete back to zero
        calculatorUnderTest.insertDigit(1);
        calculatorUnderTest.deleteLast();
        assertEquals("0", calculatorUnderTest.output());
        // insert multiple digits and operator and test delete
        calculatorUnderTest.insertDigit(1);
        calculatorUnderTest.insertDigit(2);
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.deleteLast(); // 12+ -> 12
        assertEquals("12", calculatorUnderTest.output());
        calculatorUnderTest.insertMinus();
        calculatorUnderTest.insertDigit(5);
        calculatorUnderTest.deleteLast(); // 12-5 -> 12-
        assertEquals("12-", calculatorUnderTest.output());
        calculatorUnderTest.insertDigit(2);
        calculatorUnderTest.insertEquals(); // 12-2 = 10
        calculatorUnderTest.deleteLast(); // 10 -> 1
        assertEquals("1", calculatorUnderTest.output());
        calculatorUnderTest.deleteLast();
        assertEquals("0", calculatorUnderTest.output()); // 1->0

    }

    @Test
    public void when_callingClear_then_outputShouldBeCleared()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.clear();
        assertEquals("0", calculatorUnderTest.output());
        calculatorUnderTest.insertDigit(1);
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.clear();
        assertEquals("0", calculatorUnderTest.output());
        calculatorUnderTest.insertDigit(1);
        calculatorUnderTest.insertDigit(2);
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.insertDigit(9);
        calculatorUnderTest.insertEquals();
        calculatorUnderTest.clear();
        assertEquals("0", calculatorUnderTest.output());
    }

    @Test
    public void when_savingState_should_loadThatStateCorrectly()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        // give some input
        calculatorUnderTest.insertDigit(5);
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.insertDigit(7);

        // save current state
        Serializable savedState = calculatorUnderTest.saveState();
        assertNotNull(savedState);

        // call `clear` and make sure calculator cleared
        calculatorUnderTest.clear();
        assertEquals("0", calculatorUnderTest.output());

        // load the saved state and make sure state was loaded correctly
        calculatorUnderTest.loadState(savedState);
        assertEquals("5+7", calculatorUnderTest.output());
    }

    @Test
    public void when_savingStateFromFirstCalculator_should_loadStateCorrectlyFromSecondCalculator()
    {
        SimpleCalculatorImpl firstCalculator = new SimpleCalculatorImpl();
        SimpleCalculatorImpl secondCalculator = new SimpleCalculatorImpl();
        // give some input
        firstCalculator.insertDigit(5);
        firstCalculator.insertPlus();
        firstCalculator.insertDigit(7);

        // save current state
        Serializable savedState = firstCalculator.saveState();
        assertNotNull(savedState);

        // call `clear` and make sure calculator cleared
        secondCalculator.clear();
        assertEquals("0", secondCalculator.output());

        // load the saved state and make sure state was loaded correctly
        secondCalculator.loadState(savedState);
        assertEquals("5+7", secondCalculator.output());
        secondCalculator.insertEquals();
        savedState = secondCalculator.saveState();
        firstCalculator.loadState(savedState);
        assertEquals("12", firstCalculator.output());
    }

    @Test
    public void testInsertDigitInput()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        boolean passed = false;
        try
        {
            calculatorUnderTest.insertDigit('c');
        } catch (RuntimeException e)
        {
            passed = true;
        }
        assertTrue(passed);
        passed = false;
        try
        {
            calculatorUnderTest.insertDigit(200);
        } catch (RuntimeException e)
        {
            passed = true;
        }
        assertTrue(passed);
    }

    @Test
    public void testDoubleOperators()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.insertMinus();
        assertEquals("0+", calculatorUnderTest.output());
        calculatorUnderTest.clear();
        calculatorUnderTest.insertMinus();
        calculatorUnderTest.insertPlus();
        assertEquals("0-", calculatorUnderTest.output());
    }

    @Test
    public void testOperatorOnEquals()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertPlus(); // 0+
        calculatorUnderTest.insertDigit(9); // 0+9
        calculatorUnderTest.insertMinus(); // 0+9-
        calculatorUnderTest.insertEquals(); // 9
        assertEquals("9", calculatorUnderTest.output());
    }

    @Test
    public void flowTest1()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertDigit(5); // 5
        calculatorUnderTest.clear(); // 0
        calculatorUnderTest.insertDigit(4); // 4
        calculatorUnderTest.insertPlus(); // +
        calculatorUnderTest.insertDigit(6); // 6
        assertEquals("4+6", calculatorUnderTest.output());
        calculatorUnderTest.insertEquals(); // 10
        assertEquals("10", calculatorUnderTest.output());
    }

    @Test
    public void flowTest2()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertDigit(1); // 1
        calculatorUnderTest.insertDigit(2); // 12
        calculatorUnderTest.insertMinus(); // 12-
        calculatorUnderTest.insertDigit(1); // 12-1
        calculatorUnderTest.insertDigit(3); // 12-13
        calculatorUnderTest.insertPlus(); // 123-13+
        calculatorUnderTest.insertDigit(1); // 12-13+1
        calculatorUnderTest.insertDigit(3); // 12-13+13
        calculatorUnderTest.insertEquals(); // 12
        assertEquals("12", calculatorUnderTest.output());
    }

    @Test
    public void flowTest3()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertDigit(8); // 8
        calculatorUnderTest.insertPlus(); // 8+
        calculatorUnderTest.insertDigit(7); // 8+7
        calculatorUnderTest.insertEquals(); // 15
        calculatorUnderTest.insertMinus(); // 15-
        calculatorUnderTest.insertDigit(5); // 15-5
        assertEquals("15-5", calculatorUnderTest.output());
    }

    @Test
    public void flowTest4()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertDigit(9); // 9
        calculatorUnderTest.insertDigit(9); // 99
        calculatorUnderTest.insertMinus(); // 99-
        calculatorUnderTest.insertDigit(8); // 99-8
        calculatorUnderTest.insertDigit(8); // 99-88
        calculatorUnderTest.insertMinus(); // 99-88-
        calculatorUnderTest.insertDigit(2); // 99-88-2
        calculatorUnderTest.insertDigit(2); // 99-88-22
        calculatorUnderTest.insertEquals(); // -11
        assertEquals("-11", calculatorUnderTest.output());
        calculatorUnderTest.insertMinus(); // -11-
        calculatorUnderTest.insertDigit(2); // -11-2
        calculatorUnderTest.insertDigit(2); // -11-22
        assertEquals("-11-22", calculatorUnderTest.output());
        calculatorUnderTest.insertEquals();
        assertEquals("-33", calculatorUnderTest.output());
    }

    @Test
    public void flowTest5()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertDigit(7);
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.insertDigit(2);
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.deleteLast();
        calculatorUnderTest.deleteLast();
        assertEquals("7+", calculatorUnderTest.output());
    }

    @Test
    public void flowTest6()
    {
        SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
        calculatorUnderTest.insertDigit(1);
        calculatorUnderTest.insertDigit(2);
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.insertDigit(3);
        calculatorUnderTest.insertMinus();
        calculatorUnderTest.deleteLast();
        calculatorUnderTest.deleteLast();
        calculatorUnderTest.deleteLast();
        calculatorUnderTest.deleteLast();
        calculatorUnderTest.insertDigit(5);
        calculatorUnderTest.insertEquals();
        calculatorUnderTest.insertPlus();
        calculatorUnderTest.insertDigit(3);
        assertEquals("15+3", calculatorUnderTest.output());
        calculatorUnderTest.insertEquals();
        assertEquals("18", calculatorUnderTest.output());
    }

    @Test
    public void flowTest7()
    {
        SimpleCalculatorImpl c1 = new SimpleCalculatorImpl();
        c1.insertDigit(1); // c1 1
        c1.insertDigit(2); // c1 12
        c1.insertPlus(); // c1 12+
        c1.insertDigit(1); // c1 12+1
        Serializable state = c1.saveState();
        assertEquals("12+1", c1.output());
        SimpleCalculatorImpl c2 = new SimpleCalculatorImpl();
        c2.loadState(state);
        c1.insertEquals();
        assertEquals("12+1", c2.output());
        assertEquals("13", c1.output());
        c1.insertDigit(1); // c1 131
        c1.insertMinus(); // c1 131-
        c1.insertEquals(); // 131
        assertEquals("131", c1.output());
        c2.insertPlus(); // c2 12+1+
        c2.insertDigit(7); // c2 12+1+7
        state = c2.saveState();
        c1.loadState(state);
        assertEquals("12+1+7", c1.output());
        c2.insertEquals(); // c2 20
        assertEquals("20", c2.output());
    }

}