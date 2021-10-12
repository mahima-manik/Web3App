pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";
import "@openzeppelin/contracts/utils/Counters.sol";

contract MyNFT is ERC721 {

    using Counters for Counters.Counter;
    Counters.Counter private _tokenIds;

    constructor(string memory _name, string memory _symbol) public
        ERC721(_name, _symbol) { }

    function _baseURI() internal override view virtual
        returns (string memory) {
        return "ipfs://";
    }

    function mintToken(address owner, string memory metadataURI) public
        returns (uint256)
    {
        _tokenIds.increment();

        uint256 id = _tokenIds.current();
        super._safeMint(owner, id);
//        super._setTokenURI(id, metadataURI);

        return id;
    }
}
