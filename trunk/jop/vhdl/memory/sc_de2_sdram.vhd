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
   -- SimpCon bus
   sc_addr		: in std_logic_vector(SC_ADDR_SIZE-1 downto 0);
   sc_wr_data	: in std_logic_vector(31 downto 0);
   sc_rd		: in std_logic;
   sc_wr		: in std_logic;
   sc_rd_data	: out std_logic_vector(31 downto 0);
   sc_rdy_cnt	: out unsigned(1 downto 0)
   );
end component;

	signal ext_dram_bank : std_logic_vector(1 downto 0);


begin

process(ext_dram_bank) begin
	ext_dram_ba_0 <= ext_dram_bank(0);
	ext_dram_ba_1 <= ext_dram_bank(1);
end process;

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

	sc_addr => sc_mem_out.address(SC_ADDR_SIZE-2 downto 0)&"0",
	sc_wr_data => sc_mem_out.wr_data,
	sc_rd => sc_mem_out.rd,
	sc_wr => sc_mem_out.wr,
	sc_rd_data => sc_mem_in.rd_data,
	sc_rdy_cnt => sc_mem_in.rdy_cnt

   );


end rtl;
