package cardlogic;

import lists.ArrayIndexedSortedList;

public class Hand implements Comparable<Hand>{
	//private Card[] hand;
	private ArrayIndexedSortedList<Card> hand;
	
	private int[] values;
	
	private HandRank rank;
	
	private int currentIndex;
	
	private Card highCard;
	
	public Hand(){
		currentIndex = 0;
		
		hand = new ArrayIndexedSortedList<Card>();
		
		values = new int[13];
		
		for(int i = 0; i < values.length; i++){
			values[i] = 0;
		}
	}
	
	public Card nextCard() throws Exception{
		
		if(currentIndex >= hand.size()){
			throw new Exception();
		}
		return hand.get(currentIndex++);
	}
	
	public void reset(){
		currentIndex = 0;
	}
	
	public Hand(Card[]cards){
		
		this();
		
		for(int i = 0; i < cards.length; i++){
			hand.add(cards[i]);
		}
		
	}
	
	public void addCard(Card c){
		hand.add(c);
	}
	
	public void clearHand(){
		hand.clear();
	}
	
	public int numCards(){
		return hand.size();
	}
	
	public Card getCard(int index) throws Exception{
		if(index < hand.size()){
			return hand.get(index);
		}else{
			throw new Exception();
		}
	}
	
	public Card getHighCard(){
		return highCard;
	}

	public int[]getValues(){
		return values;
	}
	
	public Value getMultipleVal(int multiple){
		for(int i = 0; i < values.length; i++){
			System.out.println(values[i]);
			if(values[i] == multiple){
				System.out.println(i);
				return Value.values()[i];
			}
		}
		return null;
	}
	
	private int highCardResult(Hand h1, Hand h2){
		try{
			for(int i = h1.numCards() - 1; i >= 0; i--){
				int result = h1.getCard(i).compareTo(h2.getCard(i));
				
				if(result != 0){
					return result;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int compareTo(Hand otherHand) {
		
		if(rank.ordinal() == otherHand.getHandRank().ordinal()){
			
			try{
			
				switch(rank){
				case FLUSH:
				case ROYAL_FLUSH:
				case STRAIGHT:
				case STRAIGHT_FLUSH:
					return highCard.compareTo(otherHand.getHighCard());
				case HIGH_CARD:
					
					return highCardResult(this, otherHand);
					
				case ONE_PAIR:
					
					Value myPairVal = getMultipleVal(2);
					Value otherPairVal = otherHand.getMultipleVal(2);
					
					if(myPairVal.ordinal() > otherPairVal.ordinal()){
						return 1;
					}else if(myPairVal.ordinal() < otherPairVal.ordinal()){
						return -1;
					}else{
						return highCardResult(this, otherHand);
					}
				case FULL_HOUSE:
				case THREE_KIND:
					Value myThreeVal = getMultipleVal(3);
					Value otherThreeVal = otherHand.getMultipleVal(3);
					
					if(myThreeVal.ordinal() > otherThreeVal.ordinal()){
						return 1;
					}else if(myThreeVal.ordinal() < otherThreeVal.ordinal()){
						return -1;
					}else{
						return highCardResult(this, otherHand);
					}
				case FOUR_KIND:
					Value myFourVal = getMultipleVal(4);
					Value otherFourVal = otherHand.getMultipleVal(4);
					
					if(myFourVal.ordinal() > otherFourVal.ordinal()){
						return 1;
					}else if(myFourVal.ordinal() < otherFourVal.ordinal()){
						return -1;
					}else{
						return highCardResult(this, otherHand);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				return 0;
			}
				
			return 0;
		}else if(rank.ordinal() < otherHand.getHandRank().ordinal()){
			return -1;
		}else{
			return 1;
		}
	}
	
	public void resetValues(){
		for(int i = 0; i < values.length; i++){
			values[i] = 0;
		}
		highCard = null;
	}
	
	public Card[] getCards(){
		Card[]c = new Card[hand.size()];
		
		for(int i = 0; i < hand.size(); i++){
			c[i] = hand.get(i);
		}
		
		return c;
	}
	
	public void updateRank(){
		
		resetValues();
		
		highCard = hand.get(hand.size()-1);
		
		for(int i = 0; i < hand.size(); i++){
			values[hand.get(i).getValue().ordinal()]++;
		}
		
		//0
		//0
		//0
		//0 -> 1
		//0 -> 1
		//0
		//0
		//0	-> 2
		//0
		//0
		//0 -> 1
		//0
		//0
		
		Value twoKind = null, twoKind2 = null, threeKind = null, fourKind = null;
		Value[] vals = Value.values();
			
		
		/*
		 * PAIRS, THREE OF KIND, FOUR OF KIND
		 */
		for(int i = 0; i < values.length; i++){
			if(values[i] == 2){
				/*
				 * A pair
				 */
				if(twoKind == null){
					twoKind = vals[i];
				}else{
					twoKind2 = vals[i];
				}
			}else if(values[i] == 3){
				/*
				 * Three of a kind
				 */
				threeKind = vals[i];
			}else if(values[i] == 4){
				/*
				 * Four of a kind
				 */
				fourKind = vals[i];
			}
		}
		
		/*
		 * FLUSH
		 */
		boolean flush = true;

		for(int i = 0; i < hand.size() - 1; i++){
			if(hand.get(i).getSuit().ordinal() != hand.get(i+1).getSuit().ordinal()){
				flush = false;
			}
		}
		
		/*
		 * STRAIGHT
		 */
		boolean straight = true;
		
		for(int i = 0; i < hand.size() - 1; i++){
			if(hand.get(i).getValue().ordinal() +1 != hand.get(i + 1).getValue().ordinal()){
				straight = false;
				break;
			}
		}
		
		//HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
		if(straight && highCard.getValue() == Value.ACE && flush){
			rank = HandRank.ROYAL_FLUSH;
		}else if(straight && flush){
			rank = HandRank.STRAIGHT_FLUSH;
		}else if(fourKind != null){
			rank = HandRank.FOUR_KIND;
		}else if(threeKind != null && twoKind != null){
			rank = HandRank.FULL_HOUSE;
		}else if(flush){
			rank = HandRank.FLUSH;
		}else if(straight){
			rank = HandRank.STRAIGHT;
		}else if(threeKind != null){
			rank = HandRank.THREE_KIND;
		}else if(twoKind != null && twoKind2 != null){
			rank = HandRank.TWO_PAIR;
		}else if(twoKind != null){
			rank = HandRank.ONE_PAIR;
		}else{
			rank = HandRank.HIGH_CARD;
		}
	}
	
	public String toString(){
		String s = "";
		for(int i = 0; i < hand.size(); i++){
			s += hand.get(i) + "\n";
		}
		return s;
	}
	
	public HandRank getHandRank(){
		return rank;
	}
	
	public void swapCardAtPosition(int pos, Card c){
		hand.remove(hand.get(pos));
		hand.add(c);
	}
}
