package it.uniroma2.dicii.isw2.jcs.paramTests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.junit.Before;
import org.junit.Test;

public class TestJCSRemovalSimpleConcurrentParam {
	private int count;
	private JCS jcs;
	
	@Before
	public void configure() throws CacheException {
		this.count = 800;
		JCS.setConfigFilename( "/TestRemoval.ccf" );
		JCS.getInstance( "testCache1" );
		this.jcs = JCS.getInstance( "testCache1" );
	}

	
	@Test
	public void testTwoDeepRemoval() throws Exception {

		System.out.println( "------------------------------------------" );
		System.out.println( "testTwoDeepRemoval" );

		for ( int i = 0; i <= count; i++ ){
			jcs.put( "key:" + i + ":anotherpart", "data" + i );
		}

		for ( int i = count; i >= 0; i-- ){
			String res = (String) jcs.get( "key:" + i + ":anotherpart" );
			if ( res == null ){
				assertNotNull( "[key:" + i + ":anotherpart] should not be null, " + jcs.getStats(), res );
			}
		}
		System.out.println( "Confirmed that " + count + " items could be found" );

		for ( int i = 0; i <= count; i++ ){
			jcs.remove( "key:" + i + ":" );
			assertNull( jcs.getStats(), jcs.get( "key:" + i + ":anotherpart" ) );
		}
		System.out.println( "Confirmed that " + count + " items were removed" );

		System.out.println( jcs.getStats() );

	}

	/**
	 * Verify that 1 level deep hierchical removal works.
	 *
	 * @throws Exception
	 */
	
	@Test
	public void testSingleDepthRemoval() throws Exception {

		System.out.println( "------------------------------------------" );
		System.out.println( "testSingleDepthRemoval" );

		for ( int i = 0; i <= count; i++ ){
			jcs.put( i + ":key", "data" + i );
		}

		for ( int i = count; i >= 0; i-- ){
			String res = (String) jcs.get( i + ":key" );
			if ( res == null ){
				assertNotNull( "[" + i + ":key] should not be null", res );
			}
		}
		System.out.println( "Confirmed that " + count + " items could be found" );

		for ( int i = 0; i <= count; i++ ){
			jcs.remove( i + ":" );
			assertNull( jcs.get( i + ":key" ) );
		}
		System.out.println( "Confirmed that " + count + " items were removed" );

		System.out.println( jcs.getStats() );

	}

	/**
	 * Verify that clear removes everyting as it should.
	 *
	 * @throws Exception
	 */
	
	@Test
	public void testClear() throws Exception{

		System.out.println( "------------------------------------------" );
		System.out.println( "testRemoveAll" );

		for ( int i = 0; i <= count; i++ ){
			jcs.put( i + ":key", "data" + i );
		}

		for ( int i = count; i >= 0; i-- ){
			String res = (String) jcs.get( i + ":key" );
			if ( res == null ){
				assertNotNull( "[" + i + ":key] should not be null", res );
			}
		}
		System.out.println( "Confirmed that " + count + " items could be found" );

		System.out.println( jcs.getStats() );

		jcs.clear();

		for ( int i = count; i >= 0; i-- ){
			String res = (String) jcs.get( i + ":key" );
			if ( res != null ){
				assertNull( "[" + i + ":key] should be null after remvoeall" + jcs.getStats(), res );
			}
		}
		System.out.println( "Confirmed that all items were removed" );

	}

	/**
	 * Verify that we can clear repeatedly without error.
	 *
	 * @throws Exception
	 */
	
	@Test
	public void testClearRepeatedlyWithoutError() throws Exception{

		System.out.println( "------------------------------------------" );
		System.out.println( "testRemoveAll" );

		jcs.clear();

		for ( int i = 0; i <= count; i++ ){
			jcs.put( i + ":key", "data" + i );
		}

		for ( int i = count; i >= 0; i-- ){
			String res = (String) jcs.get( i + ":key" );
			if ( res == null ){
				assertNotNull( "[" + i + ":key] should not be null", res );
			}
		}
		System.out.println( "Confirmed that " + count + " items could be found" );

		System.out.println( jcs.getStats() );

		for ( int i = count; i >= 0; i-- ){
			jcs.put( i + ":key", "data" + i );
			jcs.clear();
			String res = (String) jcs.get( i + ":key" );
			if ( res != null ){
				assertNull( "[" + i + ":key] should be null after remvoeall" + jcs.getStats(), res );
			}
		}
		System.out.println( "Confirmed that all items were removed" );

	}
}
