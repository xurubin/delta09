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
--	sc_de2.vhd
--
--	Peripherals for DE2 eductaion board
--	including LEDs, Keys, Switches.
--	Author: Rubin Xu
--
--
--	resources on Cyclone
--
--		xx LCs, max xx MHz
--
--
--	2009-01-18	first version
--
--	todo:
--
--


library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity sc_de2 is
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

--	DE2 Switches and LEDs
	SW			: in std_logic_vector(17  downto 0);
	KEY			: in std_logic_vector(3   downto 0);
	LEDR		: out std_logic_vector(17 downto 0);
	LEDG		: out std_logic_vector(7  downto 0);
	HEX0		: out std_logic_vector(6  downto 0);
	HEX1		: out std_logic_vector(6  downto 0);
	HEX2		: out std_logic_vector(6  downto 0);
	HEX3		: out std_logic_vector(6  downto 0);
	HEX4		: out std_logic_vector(6  downto 0);
	HEX5		: out std_logic_vector(6  downto 0);
	HEX6		: out std_logic_vector(6  downto 0);
	HEX7		: out std_logic_vector(6  downto 0)
);
end sc_de2;

architecture rtl of sc_de2 is

--	signal xyz			: std_logic_vector(31 downto 0);
--	signal cnt			: unsigned(31 downto 0);
	signal WritableMem  : std_logic_vector(95 downto 0);
	
begin

	rdy_cnt <= "00";	-- no wait states

--
--	The registered MUX is all we need for a SimpCon read.
--	The read data is stored in registered rd_data.
--
LEDR(17 downto 0) <= WritableMem(17 downto  0);
LEDG(7  downto 0) <= WritableMem(25 downto 18);
HEX0(6  downto 0) <= not WritableMem(38 downto 32);
HEX1(6  downto 0) <= not WritableMem(45 downto 39);
HEX2(6  downto 0) <= not WritableMem(52 downto 46);
HEX3(6  downto 0) <= not WritableMem(59 downto 53);
	
HEX4(6  downto 0) <= not WritableMem(70 downto 64);
HEX5(6  downto 0) <= not WritableMem(77 downto 71);
HEX6(6  downto 0) <= not WritableMem(84 downto 78);
HEX7(6  downto 0) <= not WritableMem(91 downto 85);
process(clk, reset)
begin

	if (reset='1') then
		rd_data <= (others => '0');
	elsif rising_edge(clk) then

		if rd='1' then
			if    address="0000" then
				rd_data <= "0000000000" & (not KEY) & SW;
			elsif address="0100" then
				rd_data <= WritableMem(31 downto 0);
			elsif address="1000" then
				rd_data <= WritableMem(63 downto 32);
			elsif address="1100" then
				rd_data <= WritableMem(95 downto 64);
			end if;
		end if;
	end if;

end process;


--
--	SimpCon write is very simple
--
process(clk, reset)

begin

	if (reset='1') then
		WritableMem <= (others => '0');

	elsif rising_edge(clk) then
		if wr='1' then
			if address="0100" then
				WritableMem(31 downto  0) <= wr_data;
			elsif address="1000" then
				WritableMem(63 downto 32) <= wr_data;
			elsif address="1100" then
				WritableMem(95 downto 64) <= wr_data;
			end if;
		end if;
	end if;

end process;


end rtl;