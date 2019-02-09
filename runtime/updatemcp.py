# -*- coding: utf-8 -*-
"""
Created on Fri Apr  8 16:54:36 2011

@author: ProfMobius
@version: v1.0
"""
import sys, time
from optparse import OptionParser
from commands import Commands

def main(conffile=None, force=False):
    commands = Commands(conffile)

    commands.logger.info ('== Updating MCP ==')
    commands.downloadupdates(force)

if __name__ == '__main__':
    parser = OptionParser(version='MCP %s' % Commands.MCPVersion)
    parser.add_option('-f', '--force', action='store_true', dest='force', help='force update', default=False)
    parser.add_option('-c', '--config', dest='config', help='additional configuration file')
    (options, args) = parser.parse_args()
    main(options.config, options.force)
