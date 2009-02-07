module test_verilog(
					input [17:0] SW,
					input CLOCK_50,
					output [17:0] LEDR,
					output [7:0] LEDG,
					output [6:0] HEX0,
					output [6:0] HEX1,
					output [6:0] HEX2,
					output [6:0] HEX3,
					output [6:0] HEX4,
					output [6:0] HEX5,
					output [6:0] HEX6,
					output [6:0] HEX7
					);


	//set ourselves a clear slate.					
	assign HEX0 = -1;
	assign HEX1 = -1;
	assign HEX2 = -1;
	assign HEX3 = -1;
	assign HEX4 = -1;
	assign HEX5 = -1;
	assign HEX6 = -1;
	assign HEX7 = -1;
	
	//get_clock g(CLOCK_50, 100000000, 2,LEDR[17]);
	rslatch(LEDR[17], LEDR[16], SW[17], SW[16]);
	
	rom16(LEDR[10], SW[15:12], CLOCK_50);
	

endmodule
