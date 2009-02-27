//gates

module dflip (output reg q, input d, input clk);
	always @ (posedge clk) begin
		q <= d;
	end
endmodule


module rslatch(output q1, output q2, input s, input r);
	wire a;
	wire b;
	nor(q1, r, q2);
	nor(q2, s, q1);
endmodule

module hexdisplay(output [6:0] hex, input w0, input w1, input w2, input w3, input w4, input w5, input w6);
	assign hex[0] = ~w0;
	assign hex[1] = ~w1;
	assign hex[2] = ~w2;
	assign hex[3] = ~w3;
	assign hex[4] = ~w6;
	assign hex[5] = ~w4;
	assign hex[6] = ~w5;
endmodule
