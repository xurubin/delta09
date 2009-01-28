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
--	sc_de2_sdram.vhd
--
--	SimpCon compliant external memory interface
--	for 16-bit SDRAM (e.g. Altera DE2 board)
--
--	High 16-bit word is at lower address
--
--	Connection between mem_sc and the external memory bus
--
--	memory mapping
--	
--		000000-whatever is for the size of SDRAM	external SDRAM (w mirror)	
--
--	RAM: 16 bit word
--
--

Library IEEE;
use IEEE.std_logic_1164.all;
use ieee.numeric_std.all;

use work.jop_types.all;
use work.sc_pack.all;
use work.wb_pack.all;

entity sc_sdram_if is
port (

	clk, reset			: in std_logic;

--
--	SimpCon memory interface
--
	sc_mem_out		: in sc_out_type;
	sc_mem_in		: out sc_in_type;

-- memory interface
	ext_dram_clk_i		: in std_logic;
	ext_dram_clk_locked	: in std_logic;
	ext_dram_addr		: out std_logic_vector(11 downto 0);
	ext_dram_dq			: inout std_logic_vector(15 downto 0);
	ext_dram_ba_0		: out std_logic;
	ext_dram_ba_1		: out std_logic;
	ext_dram_ldqm		: out std_logic;
	ext_dram_udqm		: out std_logic;
	ext_dram_ras_n		: out std_logic;
	ext_dram_cas_n		: out std_logic;
	ext_dram_cke		: out std_logic;
	ext_dram_clk		: out std_logic;
	ext_dram_we_n		: out std_logic;
	ext_dram_cs_n		: out std_logic
);
end sc_sdram_if;

architecture rtl of sc_sdram_if is

component sdram_controller port
  (clk_i 		: in std_logic;
   dram_clk_i	: in std_logic;
   rst_i		: in std_logic;
   dll_locked	: in std_logic;
   -- all ddr signals
   dram_addr	: out std_logic_vector(11 downto 0);
   dram_bank	: out std_logic_vector(1 downto 0);
   dram_cas_n	: out std_logic;
   dram_cke		: out std_logic;
   dram_clk		: out std_logic;
   dram_cs_n	: out std_logic;
   dram_dq		: inout std_logic_vector(15 downto 0);
   dram_ldqm	: out std_logic;
   dram_udqm	: out std_logic;
   dram_ras_n	: out std_logic;
   dram_we_n	: out std_logic;
   -- wishbone bus
   addr_i		: in std_logic_vector(21 downto 0);
   dat_i		: in std_logic_vector(31 downto 0);
   dat_o		: out std_logic_vector(31 downto 0);
   we_i			: in std_logic;
   ack_o		: out std_logic;
   stb_i		: in std_logic;
   cyc_i		: in std_logic
   );
end component;

	signal ext_dram_bank : std_logic_vector(1 downto 0);
	signal wb_mem_out	: wb_master_out_type;
	signal wb_mem_in	: wb_master_in_type;

	signal sc_bridge_out		: sc_out_type;
	signal sc_bridge_in			: sc_in_type;
	
	type sdram_state_type	is (s0, s_read_low, s_read_high, s_write_low, s_write_high);
	signal sdram_state 		: sdram_state_type;

begin

process(ext_dram_bank) begin
	ext_dram_ba_0 <= ext_dram_bank(0);
	ext_dram_ba_1 <= ext_dram_bank(1);
end process;

sc_bridge_out.address 	<= sc_mem_out.address(SC_ADDR_SIZE-2 downto 0) & "0";
sc_bridge_out.wr_data 	<= sc_mem_out.wr_data;
sc_bridge_out.rd 		<= sc_mem_out.rd;
sc_bridge_out.wr 		<= sc_mem_out.wr;
sc_bridge_out.atomic 	<= sc_mem_out.atomic;
sc_mem_in 				<= sc_bridge_in;

-- sc_mem_in.rdy_cnt <= "00" when sdram_state=s0 else "11";

-- process (reset, clk) begin
-- 	if (clk = '1') then
-- 		sdram_state <= s0;
-- 		sc_mem_in.rd_data <= (others => '0');
-- 		sc_mem_in.rdy_cny <= '0';
-- 	else
-- 		case sdram_state is
-- 			when s0 =>	-- Standby State
-- 				if (sc_mem_out.rd = '1') then
-- 					sdram_state => s_read_low;
-- 					sc_bridge_out.address <= sc_mem_out.address;
-- 					sc_bridge_out.wr_data ,<= (others => 'x');
--	 				sc_bridge_out.rd <= '1';
-- 					sc_bridge_out.wr <= '0';
-- 				elsif (sc_mem_out.wr = '1')
-- 					sdram_state => s_write_low;
-- 				end if;
-- // row width 12
--  // column width 8
-- // bank width 2
--  // user address is specified as {bank,row,column}
-- Return only 16 bit data
--	type sdram_state_type	is (s0, s_read_low, s_read_high, s_write_low, s_write_high);
-- sc_mem_in.rd_data <=
-- sc_mem_out.address
-- sc_mem_out.wr_data
-- sc_mem_out.rd
-- sc_mem_out.wr
-- 			when s_read_low =>
-- 				if (sc_bridge_in.rdy_cnt = '00') then
-- 				end if;
-- 	end if;
-- end process;

sdram: sdram_controller port map
  (clk_i => clk,
   dram_clk_i => ext_dram_clk_i,
   dll_locked => ext_dram_clk_locked,
   rst_i => reset,
   -- all ddr signals
   dram_addr => ext_dram_addr,
   dram_bank => ext_dram_bank,
   dram_cas_n => ext_dram_cas_n,
   dram_cke	=> ext_dram_cke,
   dram_clk	=> ext_dram_clk,
   dram_cs_n => ext_dram_cs_n,
   dram_dq	=> ext_dram_dq,
   dram_ldqm => ext_dram_ldqm,
   dram_udqm => ext_dram_udqm,
   dram_ras_n => ext_dram_ras_n,
   dram_we_n => ext_dram_we_n,
   -- wishbone bus
   addr_i => wb_mem_out.adr_o(21 downto 0),
   dat_i => wb_mem_out.dat_o(31 downto 0),
   we_i	=> wb_mem_out.we_o,
   stb_i => wb_mem_out.stb_o,
   cyc_i =>wb_mem_out.cyc_o,
   dat_o => wb_mem_in.dat_i(31 downto 0),
   ack_o => wb_mem_in.ack_i
   );

bus_adapter : work.sc2wb generic map(addr_bits => 22)
port map(
	clk	=>clk,
	reset => reset,

	address	=> sc_bridge_out.address(21 downto 0),
	wr_data	=> sc_bridge_out.wr_data,
	rd => sc_bridge_out.rd,
	wr => sc_bridge_out.wr,
	rd_data	=> sc_bridge_in.rd_data,
	rdy_cnt	=> sc_bridge_in.rdy_cnt,

	wb_out => wb_mem_out,
	wb_in => wb_mem_in
);
end rtl;
