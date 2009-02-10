--
--
--  This file is a part of JOP, the Java Optimized Processor
--
--  Copyright (C) 2001-2008, Martin Schoeberl (martin@jopdesign.com)
--
--  This program is free software: you can redistribute it and/or modify
--  it under the terms of the GNU General Public License as published by
--  the Free Software Foundation, either version 3 of the License, or
--  (at your option) any later version.
--
--  This program is distributed in the hope that it will be useful,
--  but WITHOUT ANY WARRANTY; without even the implied warranty of
--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--  GNU General Public License for more details.
--
--  You should have received a copy of the GNU General Public License
--  along with this program.  If not, see <http://www.gnu.org/licenses/>.
--


--
--	sc_test_slave.vhd
--
--	A simple test slave for the SimpCon interface
--	
--	Author: Martin Schoeberl	martin@jopdesign.com
--
--
--	resources on Cyclone
--
--		xx LCs, max xx MHz
--
--
--	2005-11-29	first version
--
--	todo:
--
--


library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity sc_jtag_uart is 
generic (addr_bits : integer);
port (
	clk		: in std_logic;
	reset	: in std_logic;

-- SimpCon interface

	address		: in std_logic_vector(addr_bits-1 downto 0);
	wr_data		: in std_logic_vector(31 downto 0);
	rd, wr		: in std_logic;
	rd_data		: out std_logic_vector(31 downto 0);
	rdy_cnt		: out unsigned(1 downto 0);

	debug	:out std_logic_vector(15 downto 0);

	TDI		: in std_logic;
	TCS		: in std_logic;
	TCK		: in std_logic;
	TDO		: out std_logic
	
);
end sc_jtag_uart;



architecture rtl of sc_jtag_uart is

	signal rx_buf: std_logic_vector(7 downto 0);
	signal rx_new_data: std_logic;
	
--- JTAG_USB_UART signals
	signal iTxD_DATA		:std_logic_vector(7 downto 0);
	signal oRxD_DATA		:std_logic_vector(7 downto 0);
	signal iTxD_Start		:std_logic;
	signal oTxD_Done		:std_logic;
	signal oRxD_Ready		:std_logic;
	
	type uart_rx_state_type		is (s0, s1, s2);
	signal trans_state 		: uart_rx_state_type;
	signal TxD_Buffer : std_logic_vector(7 downto 0);
	signal rf_dout : std_logic_vector(7 downto 0);
	signal rf_din : std_logic_vector(7 downto 0);
	signal rf_rd	:std_logic;
	signal rf_wr	:std_logic;
	signal rf_empty	:std_logic;
	signal rf_full	:std_logic;
	
	signal rx_ShiftRg	:std_logic_vector(455 downto 0);
	signal rx_PacketBuf	:std_logic_vector(447 downto 0);
component usb_jtag port(
	iCLK 	: in std_logic;
	iRST_n	: in std_logic;
	
	iTxD_DATA : in std_logic_vector(7 downto 0);
	oTxD_Done : out std_logic;
	iTxD_Start: in std_logic;
	oRxD_DATA : out std_logic_vector(7 downto 0);
	oRxD_Ready: out std_logic;

	TDI : in std_logic;
	TCS : in std_logic;
	TCK : in std_logic;
	TDO : out std_logic
);
end component;

component fifo is
generic (width : integer; depth : integer; thres : integer);
port (
	clk		: in std_logic;
	reset	: in std_logic;

	din		: in std_logic_vector(width-1 downto 0);
	dout	: out std_logic_vector(width-1 downto 0);

	rd		: in std_logic;
	wr		: in std_logic;

	empty	: out std_logic;
	full	: out std_logic;
	half	: out std_logic
);
end component;
begin
--cmp_rf: fifo generic map (8, 15, 8)
--		port map (clk, reset, rf_din, rf_dout, rf_rd, rf_wr, rf_empty, rf_full, rf_half);

cmp_USB_JTAG: usb_jtag 
	port map(
		iCLK => clk,
		iRST_n => reset,
		iTxD_DATA => iTxD_DATA,
		oTxD_Done => oTxD_Done,
		iTxD_Start => iTxD_Start,
		oRxD_DATA => oRxD_DATA,
		oRxD_Ready => oRxD_Ready,

		TDI => TDI,
		TCS => TCS,
		TCK => TCK,
		TDO => TDO
	);

	rdy_cnt <= "00";	-- no wait states


process(clk, reset)
begin
	if (reset='1') then
	elsif rising_edge(clk) then
	end if;
end process;

process(clk, reset)
begin
	if (reset='1') then
		rd_data <= (others => '0');

		rx_buf <= (others => '0');
		rx_new_data <= '0';
		rx_ShiftRg <= (others => '0');
		rx_PacketBuf <= (others => '0');
	elsif rising_edge(clk) then
		if (rx_ShiftRg(455 downto 448)="10101010") then
			rx_PacketBuf <= rx_ShiftRg(447 downto 0);
		end if;
		
		if (oRxD_Ready='1') then
			--if (rx_new_data = '0') then
				rx_buf <= oRxD_Data;
				rx_new_data <= '1';
			--end if;
			if (oRxD_Data /= "00000000") then --New Non-zero data arrived.
				rx_ShiftRg <= rx_ShiftRg(447 downto 0) & oRxD_Data;
			end if;
		end if;
	
		if rd='1' then
			-- that's our very simple address decoder
			if (address(3 downto 1)="000") then --Status/Data registers
				rd_data(31 downto 8) <=(others =>'0');
				if address(0)='1' then	--Read Data register
					if rx_new_data = '1' then 
						rd_data(7 downto 0) <= rx_buf;
						if (oRxD_Ready='0') then 
							rx_new_data <= '0';
						end if;
					else
						rd_data(7 downto 0) <= rx_buf;
					end if;
				else -- Read State register
					rd_data(7 downto 2) <= "000000";
					rd_data(1) <= rx_new_data;
					if (trans_state=s0) and (oTxD_Done='0') then
						rd_data(0) <= '1';
					else
						rd_data(0) <= '0';
					end if;
				end if;
			else --"0010,0011,...1111 -- ShiftRegisters
				case address(3 downto 0) is 
				  when "0010" => rd_data <= rx_PacketBuf(13*32+31 downto 13*32);
				  when "0011" => rd_data <= rx_PacketBuf(12*32+31 downto 12*32);
				  when "0100" => rd_data <= rx_PacketBuf(11*32+31 downto 11*32);
				  when "0101" => rd_data <= rx_PacketBuf(10*32+31 downto 10*32);
				  when "0110" => rd_data <= rx_PacketBuf(09*32+31 downto 09*32);
				  when "0111" => rd_data <= rx_PacketBuf(08*32+31 downto 08*32);
				  when "1000" => rd_data <= rx_PacketBuf(07*32+31 downto 07*32);
				  when "1001" => rd_data <= rx_PacketBuf(06*32+31 downto 06*32);
				  when "1010" => rd_data <= rx_PacketBuf(05*32+31 downto 05*32);
				  when "1011" => rd_data <= rx_PacketBuf(04*32+31 downto 04*32);
				  when "1100" => rd_data <= rx_PacketBuf(03*32+31 downto 03*32);
				  when "1101" => rd_data <= rx_PacketBuf(02*32+31 downto 02*32);
				  when "1110" => rd_data <= rx_PacketBuf(01*32+31 downto 01*32);
				  when "1111" => rd_data <= rx_PacketBuf(00*32+31 downto 00*32);
				  when others => rd_data <= (others => '0');
				end case;
			end if;
		end if;
	end if;
end process;

--
--	SimpCon write is very simple
--

--	status word in uarts:
--		0	uart transmit data register empty
--		1	uart read data register full
--
--	signal iTxD_DATA		:std_logic_vector(7 downto 0);
--	signal oRxD_DATA		:std_logic_vector(7 downto 0);
--	signal iTxD_Start		:std_logic_vector;
--	signal oTxD_Done		:std_logic_vector;
--	signal oRxD_Ready		:std_logic_vector;

iTxD_DATA <= TxD_Buffer;

process(clk, reset) --State Machine for Write
begin
	if (reset='1') then
		trans_state <= s0;
		iTxD_Start <= '0';
	elsif rising_edge(clk) then
		case trans_state is
		  when s0 =>  
			iTxD_Start <= '0';
			if wr='1' and address(0)='1' and oTxD_Done='0' then --REMIND: DISCARD NEW DATA WRITES.. NO QUEUE
					TxD_Buffer <= wr_data(7 downto 0);
					iTxD_Start <= '1';
					trans_state <= s1;
			end if;
		  when s1 =>
			if (oTxD_Done='1') then 
				trans_state <= s0;
				iTxD_Start <= '0';
			end if;
		  when s2 => 
			trans_state <= s0;
		end case;
	end if;
end process;


end rtl;
