/*
 * MinMax.java
 * 
 * Copyright 2012 parallels <parallels@parallels-Parallels-Virtual-Platform>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */

package player;

public class MinMax extends MachinePlayer {
	
	private Move minMax(int depth){
	MoveScore highest = null;
	Move ourMove = null;
	Move theirMove = null;
	
	if(depth == 0){
		if(internal.totalChipCount < 11){
			return addMoveScore(color);
		}else{
			return stepMoveScore(color);
		}
	}		
			
	if(internal.totalChipCount()>10){
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				if(internal.getContents(i, j) != null) continue;
			
				ourMove = new Move(i, j);
				internal.makeMove(ourMove);
				theirMove = bestMove(oppColor);
				makeMove(theirMove, oppColor);
				temp = minMax(depth--);
				if(temp.score > heighest.score){
					highest = new MoveScore(i, j, temp.score);
				}
				internal.unmakeMove(ourMove);
				internal.unmakeMove(theirMove);
			}
		}
	}else{
		
			// Loop to get existing chips
			for(int i=0; i<8; i++){
				for(int j=0; j<8; j++){
					if(internal.getContents(i, j).chipColor != color) continue;
					Chip temp = internal.getContents(i, j);
				}
			}
		
			for(int a=0; a<8; a++){
				for(int b=0; b<8; b++){
					
					if(internal.getContents(a, b).chipColor != null) continue;
		
					ourMove = new Move(a, b, i, j);
					internal.makeMove(ourMove);
					theirMove = bestMove(oppColor);
					makeMove(theirMove, oppColor);
					temp = minMax(depth--);
					if(temp.score > heighest.score){
						highest = new MoveScore( temp.score);
					}
					internal.unmakeMove(ourMove);
					internal.unmakeMove(theirMove);
			
				}
			}
		}
	
	return (Move) highest;
}
}

